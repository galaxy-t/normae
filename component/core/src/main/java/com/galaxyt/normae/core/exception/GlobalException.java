package com.galaxyt.normae.core.exception;

/**
 * 自定义全局异常
 */
public class GlobalException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    /**
     * 异常代码
     */
    private GlobalExceptionCode code;


    /**
     * 不允许只提供异常信息，所有抛出的自定义异常必须使用全局自定义异常信息
     * @param message
     */
    private GlobalException(String message) {
        super(message);
    }

    /**
     * 使用全局自定义异常信息抛出异常
     * @param code
     */
    public GlobalException(GlobalExceptionCode code) {
        super(code.getMsg());
        this.code = code;
    }

    /**
     * 使用全局自定义异常信息抛出异常并自定义异常信息内容
     * @param code
     * @param msg
     */
    public GlobalException(GlobalExceptionCode code, String msg) {
        super(msg);
        this.code = code;
    }


    public GlobalExceptionCode getCode() {
        return code;
    }

}
