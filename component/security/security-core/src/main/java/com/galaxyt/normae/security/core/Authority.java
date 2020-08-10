package com.galaxyt.normae.security.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于给一个 RequestMapping 方法赋予权限
 * 未被该注解标识的不允许被网关层穿透
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
     * 标识
     * @return
     */
    String mark();

    /**
     * 描述信息
     * @return
     */
    String name();

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
