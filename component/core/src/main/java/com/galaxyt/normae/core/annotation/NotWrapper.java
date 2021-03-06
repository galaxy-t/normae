package com.galaxyt.normae.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注某个 Controller 的接口不会被包装 , 同样该方法抛出的任何异常也不会被异常拦截重新包装
 * @author zhouqi
 * @date 2020/8/25 10:05
 * @version v1.0.0
 * @Description
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotWrapper {
}
