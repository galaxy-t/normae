package com.galaxyt.normae.sms.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * SMS 服务 Feign 客户端
 * @author zhouqi
 * @date 2020/5/28 16:03
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/28 16:03     zhouqi          v1.0.0           Created
 *
 */
@Primary
@FeignClient(value = "sms", fallbackFactory = SmsFallback.class)
public interface SmsClient {


    /**
     * 检查短信验证码是否合法
     * @param phoneNumber
     * @param captcha
     * @param captchaType
     * @return
     */
    @GetMapping("/captcha/check")
    boolean captchaCheck(@RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("captcha") String captcha,
                         @RequestParam("captchaType") Integer captchaType);


}
