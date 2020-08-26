package com.galaxyt.normae.sms.web;

import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.seurity.RegexUtil;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.sms.api.enums.CaptchaType;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
     * 发送短信验证码
     *
     * @param captchaType 短信类型 1登录 2注册 3重置
     * @param phoneNumber 手机号码
     */
    @Authority(isLogin = false, description = "短信验证码发送")
    @GetMapping("send")
    public GlobalResponseWrapper send(@NotBlank(message = "手机号码不能为空") @RequestParam("phoneNumber") String phoneNumber,
                                      @NotNull(message = "短信类型不能为空") @RequestParam("captchaType") Integer captchaType) {

        //检查手机号是否合法
        this.checkPhoneNumber(phoneNumber);

        //检查短信验证码类型是否合法
        CaptchaType.getByCode(captchaType);

        //发送短信验证码
        return this.captchaService.send(phoneNumber, MessageType.getByCode(captchaType));
    }

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

        return this.captchaService.check(phoneNumber, captcha, MessageType.getByCode(CaptchaType.getByCode(captchaType).getCode()));
    }


    /**
     * 手机号检查
     *
     * @param phoneNumber
     */
    private void checkPhoneNumber(String phoneNumber) {
        if (!RegexUtil.phoneNumber(phoneNumber)) {
            throw new GlobalException(GlobalExceptionCode.PHONE_NUMBER_FORMAT_WRONG);
        }
    }



}
