package com.galaxyt.normae.sms.web;

import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.util.seurity.RegexUtil;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.security.core.Authority;
import com.galaxyt.normae.sms.enums.CaptchaType;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 消息
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/29 10:01
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/29 10:01     zhouqi          v1.0.0           Created
 */
@Api(tags = "短信发送")
@RestController
public class MessageController {

    @Autowired
    private CaptchaService captchaService;


    /**
     * 发送短信验证码
     *
     * @param captchaType 短信类型 1登录 2注册 3重置
     * @param phoneNumber 手机号码
     */
    @Authority(mark = "captcha", name = "获取验证码", isLogin = false)
    @GetMapping("captcha")
    @ApiOperation(value = "发送短信验证码")
    public GlobalResponseWrapper captcha(@ApiParam("手机号码") @NotBlank(message = "手机号码不能为空") @RequestParam("phoneNumber") String phoneNumber,
                                         @ApiParam("短信类型 1登录 2注册 3重置") @NotNull(message = "短信类型不能为空") @RequestParam("captchaType") Integer captchaType) {

        //检查手机号是否合法
        this.checkPhoneNumber(phoneNumber);

        //检查短信验证码类型是否合法
        CaptchaType.getByCode(captchaType);

        //发送短信验证码
        return this.captchaService.send(phoneNumber, MessageType.getByCode(captchaType));
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
