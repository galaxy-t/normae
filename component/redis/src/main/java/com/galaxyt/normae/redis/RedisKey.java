package com.galaxyt.normae.redis;

/**
 * 全局的 Redis Key 的前缀或者全名必须维护到该类中
 * @author zhouqi
 * @date 2020/5/18 13:42
 * @version v1.0.0
 * @Description
 *
 *
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 13:42     zhouqi          v1.0.0           Created
 *
 */
public class RedisKey {


    /**
     * 分布式锁
     * 发送短信验证码使用
     * arg1: 短信验证码类型 code
     * arg2: 电话号码
     */
    public static final String LOCK_SMS_CAPTCHA = "LOCK-SMS-CAPTCHA-%s-%s";


    /**
     * 短信验证码发送标识
     * 用于作为一分钟倒计时的标识
     * arg1: 短信验证码类型 code
     * arg2: 电话号码
     */
    public static final String KEY_SMS_CAPTCHA_SEND_MARK = "KEY-SMS-CAPTCHA-SEND-MARK-%s-%s";

    /**
     * 某手机号某种类型的短信验证码的 key
     * arg1: 短信验证码类型 code
     * arg2: 电话号码
     */
    public static final String KEY_SMS_CAPTCHA = "KEY-SMS-CAPTCHA-%s-%s";






}
