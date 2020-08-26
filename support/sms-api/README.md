# 短信服务 Feign 接口客户端
    
    为 sms 模块提供 feign 接口调用客户端
    
## 使用

### 短信验证码校验

    boolean com.galaxyt.normae.sms.api.SmsClient.captchaCheck(String phoneNumber,String captcha,Integer captchaType)
    phoneNumber: 手机号码
    captcha : 短信验证码
    captchaType : 该手机号和短信验证码为何种短信验证码类型 , 注意该参数使用 Integer 接口(Feign 接口定义的时候不能使用枚举 , 但是具体服务的接口可以使用)
    
    返回值 : true,手机号,短信验证码,短信验证码类型校验成功   false,为校验时报
    
### 短信验证码类型

    com.galaxyt.normae.sms.api.enums.CaptchaType
    该枚举类没有实际的业务 , 仅为调用者提供类型管理 , 校验方法的 captchaType 参数 , 必须使用如 CaptchaType.LOGIN.getCode() 传入才行 , 作为约定规范存在
     
### 引用

    本模块被 sms 模块所引用
    调用方若需要使用本方法也需要在 pom 中做出引用的配置