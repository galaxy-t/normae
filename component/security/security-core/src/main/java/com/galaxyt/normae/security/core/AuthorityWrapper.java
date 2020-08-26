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
     * 访问该接口所需要的角色
     */
    private String role;

    /**
     * 访问该接口所需要的权限
     */
    private String authority;

    /**
     * 该权限的描述信息
     */
    private String description;

    /**
     * 该接口是否需要登录才能够访问
     */
    private boolean login;

    /**
     * 用于标识该接口是否需要拥有配置的角色或权限才能进行访问
     * 仅当 isLogin = true 时该属性有效
     */
    private boolean haveAuthority;

}
