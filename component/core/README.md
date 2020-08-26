# 核心代码包

    整个工程都会使用到的基础代码均放置在该目录下 
    开发人员可以根据实际业务对该模块进行扩展
    其它所有的模块都应该依赖该模块
    
## 代码

### annotation

    通用注解

    NotWrapper  因为 web 工程会经过一个响应拦截器或异常拦截器 , 针对响应结果和异常进行 JSON 的包装 , 在接口方法上使用该注解响应拦截器或异常拦截器便不再对响应结果或异常进行包装
                使用该注解之后其响应结果会原样返回 , 再出现异常的时候也会直接抛给前端 , 推荐在 Feign 接口上使用 , 直接暴露给前端的接口慎用

### enums

    通用注解 , 用来定义全局常用类型 , 全局代码在用到该种类型时必须依靠这些枚举进行值的传递 , 但是这些枚举并不能作为 MyBatisPlus 枚举类型来使用
              数据库中若存在该类字段 , 建议该字段为 int(1) 类型 , 实体类中也使用 Integer 来接口 , 作者认为这些字段通常为非业务类型字段 , 或
              这些字段的含义是不会被暴露到前端的 , 仅在 Java 代码或数据库操作上起作用 . 
    
    Deleted     删除状态类型
    Disabled    禁用状态类型

### exception

    全局异常及异常码
    
    GlobalException         全局自定义异常 , 手动抛出的异常只能使用该异常 , 其中的 code 必须存在于 GlobalExceptionCode
                            例:
                                throw new GlobalException(GlobalExceptionCode.ERROR);
                                throw new GlobalException(GlobalExceptionCode.ERROR,"这里是异常信息");     自定义异常信息
    GlobalExceptionCode     全局异常状态码 , 全局自定义异常及返回信息必须包含其中一个枚举 , 全局的异常 code 必须维护到该枚举中


### thread
    
    跟线程有关的对象均维护到该目录下
    
    CurrentUser         当前访问用户对象 , 每一个 request 请求都属于一个线程 , 发起当次 request 请求的对象的信息会维护到 CurrentUser 中
                        web 项目可以使用 requestHandler 来从请求中得到用户信息并存储到 CurrentUser 中 , 需要注意的是还需要在 responseHandler 中对 CurrentUser 的数据进行释放 , 
                        因为 Spring 使用了线程池 , 若不释放会导致 CurrentUser 被重复使用并导致线程不安全问题 . 
    GlobalThreadFactory 全局线程工厂 , 创建多线程必须要依赖线程池 , 在使用线程池的时候必须要求提供线程工厂对象 , 以便轻松的维护全部的线程

### util
    
    通用工具类 , 其中的方法要求尽量跟业务剥离开来 , 其中的方法尽可能的使用静态方法

    json Json 有关的工具类均维护到该目录下
        GsonUtil Gson 工具类 , Gson 在新的版本其对象是线程安全的 , 所以该类维护了一个私有的 Gson 对象 , 全局其它代码中不允许开发人员再自行创建 gson 实例 , 必须使用该静态 gson 实例
    math 数学操作工具类
        NumberUtil  数字操作工具类 , 其中包含一个生成指定长度随机数字字符串的方法 , 返回字符串可以使首位为 0 
    security 安全验证 , 加密等工具类
        JWTUtil Jwt工具类 , 用来生成 token 或解密 token 
        RegexUtil   通用正则表达式方法
    HttpUtil    http请求工具类

### wrapper

    全局返回值包装类

    GlobalResponseWrapper   全局返回值包装类 , 所有的 http 返回值必须为该对象 , 要返回的状态信息和内容都要维护到该类的对象中 , 其中 code 必须存在于 GlobalExceptionCode
                            http 的响应结果会在响应拦截器中包装到 GlobalResponseWrapper 的 data 属性中 , 若接口的返回值为 GlobalResponseWrapper , 则响应拦截器不会再进行包装 , 
                            若请求抛出异常 , 异常结果也会被包装秤一个 GlobalResponseWrapper 并进行返回
                            
                            

