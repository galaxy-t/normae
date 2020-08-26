# Web 项目依赖模块

    将一些通用的配置 , 和一些拦截器放在这个模块中 , web 项目直接依赖即可
    
## 配置 [config]

### Feign 配置 [Feign]

    该配置文件其实是没有用的 , 首先讲一下它是做什么用的 , 加入有一个用户操作了 A 服务 , A 服务又通过 Feign 接口调用了 B 服务 , 那么要求 B 服务可以跟 A 服务一样获取到该用户的登录状态
    首先声明这样做是没有问题的 , SpringCloud 的设计思想的话 , 貌似也是建议这么做的 , 
    要使用这个配置 , 首先得在配置文件中添加  hystrix.command.default.execution.isolation.strategy: SEMAPHORE 开启熔断器的信号量模式才行 , 然后打开该类的注释
    但是作者认为 , 实际开发中 , 两个服务的调用 , 其实就是调用者为了实现自己的业务调用了被调用者 , 那么被调用者可以被理解为是一段业务代码 , 而不要让其显得太通用 , 
    在作者的开发习惯中 Session或CurrentUser 这种调用仅出现在 Controller 层 , 如果 Service 需要用户的登录状态 , 那么就将需要的信息当作参数传递过去 , 
    所以作者把这个类注释掉了 , 但是放在这可能以后的业务需要用到 , 其实这个配置中也不仅仅是用来传递用户信息的 , 一些其他的东西也可以进行传递
    
### MyBatisPlus配置 [MyBatisPlusConfig]

    该配置打开了 MyBatisPlus 的乐观锁插件 , 使用方法参考 optimisticLockerInterceptor() 的注释
    开启了 MyBatisPlus 的分页插件
    
### Swagger配置 [SwaggerConfig]

    在实际开发过程中 , 开发人员可以根据实际业务场景对该类中的一些内容进行修改 , 默认要求每一个接口可以提供一个 Authorization 为 key 的请求头 , 以便传递 token , 该参数非必填 , 
    有同事为了方便搭配网关实现了多模块模式 , 其实让我来感觉 , 这玩意还是少侵入点代码比较好 , 其只不过就是个文档 , 实在不行手动写个 MD 文件来展示 , 也比为其增加代码要好 .
    
### Web配置 [WebAppConfig]

    因为网关已经进行过跨域的处理 , 所以该类中仅进行了拦截器的规则设置 , 设置出了 Swagger 的请求外其它请求全部经过拦截器
    
## 拦截器 [handler]

    做了这么多项目 , 用过的也就这么三个了

### 请求拦截器 [GlobalRequestHandler]

    其中请求前置拦截方法解析出请求头中的 userId和username , 若需要其它的 , 开发人员可自行扩展 , 取出的结果放到 CurrentUser 中
    请求后置拦截用来销毁 CurrentUser 中的数据 , 以防止被线程池重复利用
    注: 虽然用户状态是依靠 JWT 来保存的 , 但是 TOKEN 在网关已经被解密 , 然后网关将解密的结果放到 Header 中传递到了当前服务 , 所以此处不存在 TOKEN 解密的代码
    
### 响应拦截器 [GlobalResponseHandler]

    用于处理一次请求的响应结果
    注: 若接口方法上使用 @NotWrapper 进行标注 , 那么该响应拦截器不再对该接口的响应结果进行处理
        若接口的响应结果为 GlobalResponseWrapper 类型 , 那么该响应拦截器不再对该接口的响应结果进行处理
        若接口的响应结果为 String 类型 , 那么该响应拦截器不再对该接口的响应结果进行处理
        若响应结果为 void , 那么该响应拦截器返回一个默认的 GlobalResponseWrapper 实例 (GlobalResponseWrapper 实例默认为 SUCCESS , data 为 null)
        其它类型该响应拦截器会返回一个 code 为 SUCCESS 的 GlobalResponseWrapper 实例 , data 为响应结果
        
### 异常拦截器 [GlobalExceptionHandler]

    该异常拦截器仅拦截 全局自定义异常(GlobalException),注解参数异常,集合中的注解参数异常
    全局自定义异常: 基本上这种情况为开发人员自行抛出的异常 , 返回结果事将异常中的异常码作为 GlobalResponseWrapper 的code , 异常的 msg 作为 GlobalResponseWrapper 的 msg , data 为 null 进行返回
    注解参数异常:   这种情况 , 返回的 GlobalResponseWrapper 的 code 为 com.galaxyt.normae.core.exception.GlobalExceptionCode.REQUEST_ARGUMENT_EXCEPTION.getCode(); , msg 信息为 , 第一个错误的信息
    集合中的注解参数异常: 通注解参数异常
    注: 
        若接口使用 @NotWrapper 进行过标注 , 则该异常拦截器不会对异常进行处理
        其它如 404,500 等异常 , 该异常拦截器不会对异常进行处理
    
    作者认为 , 类似于 500 这种异常 , 是由于开发人员的开发失误造成的 , 本质上是由于没有在开发过程中考虑周全导致 , 是一个 bug , 这些问题应该在测试环节和准生产环节进行发现与闭环 ,  
    这些异常抛出到前端 , 虽然会显得不太友好 , 但是也不能够对其进行异常包裹 , 就应该让其抛出 , 交由前端来处理 , 
    前端开发人员在处理该类情况时 , 应该对 404,500 这种不可预测的异常进行单独处理并上报给异常系统