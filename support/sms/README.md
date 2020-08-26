# 短信服务

    项目标识: sms
    默认端口: 8083

    提供短信发送服务
    提供短信验证码发送及检查
    其它方式的短信发送需开发人员另行扩展
    
## 阿里云

    sms 模块对接的短信发送平台为阿里云短信服务平台
    使用 AliyunJavaSDK 开发短信发送功能
    
## 使用

### 注册

    默认代码中仅提供
    短信验证码发送接口   com.galaxyt.normae.sms.web.CaptchaController.send
    短信验证码检查接口   com.galaxyt.normae.sms.web.CaptchaController.check
    并提供一个 feign 客户端 com.galaxyt.normae.api.sms.SmsClient.captchaCheck
    
### 短信验证码

    默认同类型(com.galaxyt.normae.sms.api.enums.CaptchaType)短信验证码过期时间为 10 分钟
    默认同类型(com.galaxyt.normae.sms.api.enums.CaptchaType)短信验证码发送时间间隔为 1 分钟
    
    每天同一手机号发送的条数代码中不需要做出限制 , 阿里云平台会进行控制(具体参见阿里云官方文档)
    
## 数据库 [normae_sms.sql](normae_sms.sql)

    默认的数据库名为 normae_sms , 实际开发人员可以自行定义该名称 , 并在配置文件中进行修改即可
    数据库中的表为作者根据已经解决过的实际业务整理出来的最精简表及字段 , 多数情况不足以支撑实际业务开发需要 , 开发人员需要自行扩展数据库及实体类
    作者感觉短信记录这个东西的话 , 也不需要做的太复杂 , 除非产品设计上对该功能有所深入的设计
    
### 短信息记录表 [t_message]
    
     CREATE TABLE `normae_sms`.`t_message` (
     `id` bigint(20) NOT NULL,
     `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接收短信的手机号码',
     `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信发送内容,实际上存储的是短信模版参数字符串',
     `message_type` int(1) NOT NULL COMMENT '短信发送类型',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     PRIMARY KEY (`id`) 
     )
     ENGINE = InnoDB
     AVG_ROW_LENGTH = 0
     DEFAULT CHARACTER SET = utf8mb4
     COLLATE = utf8mb4_0900_ai_ci
     KEY_BLOCK_SIZE = 0
     MAX_ROWS = 0
     MIN_ROWS = 0
     ROW_FORMAT = Dynamic;
     
## 注意


     
