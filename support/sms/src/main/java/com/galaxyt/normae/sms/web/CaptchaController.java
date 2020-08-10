package com.galaxyt.normae.sms.web;

import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码功能
 * @author zhouqi
 * @date 2020/5/29 10:51
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 10:51     zhouqi          v1.0.0           Created
 *
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {


    @Autowired
    private CaptchaService captchaService;

    /**
     * 检查短信验证码是否合法
     * 仅供 Feign 内部调用使用 , 不对外开放
     *
     * @param phoneNumber
     * @param captcha
     * @param captchaType
     * @return
     */
    @NotWrapper
    @GetMapping("check")
    public boolean check(@RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("captcha") String captcha,
                         @RequestParam("captchaType") Integer captchaType) {

        return this.captchaService.check(phoneNumber, captcha, MessageType.getByCode(captchaType));
    }






}
