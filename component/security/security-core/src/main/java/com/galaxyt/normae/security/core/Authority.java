package com.galaxyt.normae.security.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 用于标注一个 RequestMapping 方法 , 并同时为该接口设置访问权限
 * 未被该注解标识的接口不允许被网管层转发
 *
 * @author zhouqi
 * @date 2020/6/9 16:47
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/6/9 16:47     zhouqi          v1.0.0           Created
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {


    /**
     * 所需要的角色
     * 以英文逗号分隔
     * 用于标识拥有哪些角色才可以访问该接口
     *
     * role 和 authority 具有同等效力 , 只要用户符合 role 或 authority 中的一个条件即可
     * @return
     */
    String role();

    /**
     * 所需要的权限
     * 以英文逗号分隔
     * 用于标识拥有哪些权限才可以访问该接口
     *
     * authority 和 role 具有同等效力 , 只要用户符合 authority 或 role 中的一个条件即可
     * @return
     */
    String authority();

    /**
     * 描述信息
     * @return
     */
    String description();

    /**
     * 是否需要登录状态
     * 默认为 true
     * 设置为 false 可以达到游客的效果
     * @return
     */
    boolean isLogin() default true;

    /**
     * 是否需要拥有权限
     * 默认为 true
     * 设置为 false 可以达到本接口不需要配置的场景
     * 仅当 isLogin = true 时 , 该属性才生效
     * @return
     */
    boolean isHaveAuthority() default true;




}
