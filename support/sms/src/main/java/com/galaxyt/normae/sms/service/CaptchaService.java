package com.galaxyt.normae.sms.service;

import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.math.NumberUtil;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.redis.JedisClient;
import com.galaxyt.normae.redis.JedisLock;
import com.galaxyt.normae.redis.RedisKey;
import com.galaxyt.normae.sms.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 短信验证码 service
 * @author zhouqi
 * @date 2020/5/29 10:02
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 10:02     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Service
public class CaptchaService {



    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private AliyunService aliyunService;

    /**
     * 短信发送时间间隔
     */
    @Value("${message.send.interval}")
    private Integer messageSendInterval;

    /**
     * 短信验证码有效期
     */
    @Value("${captcha.expiration}")
    private Integer captchaExpiration;

    /**
     * 短信验证码发送
     * @param phoneNumber
     * @param messageType
     * @return
     */
    @Transactional
    public GlobalResponseWrapper send(String phoneNumber, MessageType messageType) {

        //打开分布式锁 , 粒度控制在某个手机号的某种类型
        JedisLock jedisLock = new JedisLock(this.jedisClient.getJedis(), String.format(RedisKey.LOCK_SMS_CAPTCHA, messageType.getCode(), phoneNumber));

        //用于记录是否得到锁
        boolean isHaveLock = false;
        try {
            isHaveLock = jedisLock.lock();
        } catch (InterruptedException e) {  //可能会存在线程睡眠异常 , 忽略这个异常
            log.error("获取分布式锁失败,向手机号[{}]发送短信验证码[{}]失败[{}]", phoneNumber, messageType.getMsg(), e.getMessage());
            e.printStackTrace();
        }

        /*
        获取不到锁的原因可能有很多种
        不仅仅是线程休眠异常造成的
        也可能是其它线程长时间占有锁导致获取失败
        所以需要在此处再次判断一下
         */
        if (!isHaveLock) {
            log.error("获取分布式锁竞争失败,向手机号[{}]发送短信验证码[{}]", phoneNumber, messageType.getMsg());
            return new GlobalResponseWrapper(GlobalExceptionCode.MESSAGE_SEND_ERROR);
        }

        //以下情况为拥有锁的情况

        //检查短信验证码是否存在 , 若存在则反馈短信验证码发送频繁
        boolean captchaSendMarkExists = this.jedisClient.operate(jedis -> jedis.exists(String.format(RedisKey.KEY_SMS_CAPTCHA_SEND_MARK, messageType.getCode(), phoneNumber)));
        if (captchaSendMarkExists) {
            return new GlobalResponseWrapper(GlobalExceptionCode.MESSAGE_SEND_FREQUENTLY);
        }

        //得到一个长度为 6 的随机数字符串
        String captcha = NumberUtil.random(6);

        //若短信发送失败则直接反馈
        boolean isSend = this.aliyunService.sendMessage(phoneNumber, messageType, String.format(messageType.getTemplateParams(), captcha));
        if (!isSend) {
            return new GlobalResponseWrapper(GlobalExceptionCode.MESSAGE_SEND_ERROR);
        }

        //当前事物后置处理
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

            //等待事物提交完成执行
            @Override
            public void afterCommit() {
                //将短信验证码发送标识和短信验证法存入 redis 并设置过期时间
                CaptchaService.this.jedisClient.operate(jedis -> {
                    jedis.setex(String.format(RedisKey.KEY_SMS_CAPTCHA_SEND_MARK, messageType.getCode(), phoneNumber), CaptchaService.this.messageSendInterval, captcha);
                    jedis.setex(String.format(RedisKey.KEY_SMS_CAPTCHA, messageType.getCode(), phoneNumber), CaptchaService.this.captchaExpiration, captcha);
                    return null;    //此函数要求必须存在返回值 , 此处返回一个 null
                });
            }

            //全部事物后置方法结束之后执行
            @Override
            public void afterCompletion(int status) {
                //等待本方法全部结束释放分布式锁
                jedisLock.unlock();
            }
        });

        return new GlobalResponseWrapper();
    }



    /**
     * 检查短信验证码是否正确
     * @param phoneNumber
     * @param captcha
     * @param messageType
     * @return
     */
    public boolean check(String phoneNumber, String captcha, MessageType messageType) {

        String oldCaptcha = this.jedisClient.operate(jedis -> jedis.get(String.format(RedisKey.KEY_SMS_CAPTCHA, messageType.getCode(), phoneNumber)));

        if (captcha.equals(oldCaptcha))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }


}
