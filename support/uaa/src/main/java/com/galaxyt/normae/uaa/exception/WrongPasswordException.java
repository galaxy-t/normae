package com.galaxyt.normae.uaa.exception;

/**
 * 密码错误异常
 * @author zhouqi
 * @date 2020/5/21 11:55
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/21 11:55     zhouqi          v1.0.0           Created
 *
 */
public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        super("密码错误");
    }

    public WrongPasswordException(String msg) {
        super(msg);
    }


}
