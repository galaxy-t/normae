package com.galaxyt.normae.uaa.exception;

/**
 * 用户名找不到时抛出的异常
 * @author zhouqi
 * @date 2020/5/21 11:48
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/21 11:48     zhouqi          v1.0.0           Created
 *
 */
public class UsernameNotFoundException extends Exception {

    public UsernameNotFoundException() {
        super("用户名未找到");
    }

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

}
