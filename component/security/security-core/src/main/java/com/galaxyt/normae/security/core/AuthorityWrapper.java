package com.galaxyt.normae.security.core;


import lombok.Data;

/**
 * 用于封装一个请求的基本信息
 * @author zhouqi
 * @date 2020/6/9 17:02
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/9 17:02     zhouqi          v1.0.0           Created
 *
 */
@Data
public class AuthorityWrapper {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 类型 , 如 GET POST PUT 等
     */
    private String method;

    /**
     * 权限标识 , 若两个权限同时拥有一个接口则以逗号分隔
     */
    private String mark;

    /**
     * 该权限的描述信息
     */
    private String name;

    /**
     * 该接口是否需要登录才能够访问
     */
    private boolean login;

    /**
     * 该接口是否需要拥有该权限才能够访问
     * 仅当 isLogin = true 时该属性有效
     */
    private boolean haveAuthority;

}
