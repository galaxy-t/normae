package com.galaxyt.normae.api.sms.enums;

/**
 * 验证码类型
 * @author zhouqi
 * @date 2020/5/29 10:10
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 10:10     zhouqi          v1.0.0           Created
 *
 */
public enum CaptchaType {

    LOGIN(1, "登录"),
    REGISTER(2, "注册"),
    RESET(3, "重置"),

            ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 内容
     */
    private final String msg;


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    CaptchaType(int code, String msg) {
        this.code = code;
        this.msg = msg;

    }
}
