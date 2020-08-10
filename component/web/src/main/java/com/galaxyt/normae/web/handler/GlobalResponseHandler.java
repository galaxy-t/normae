package com.galaxyt.normae.web.handler;

import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

/**
 * 全局返回值统一包装
 * @author zhouqi
 * @date 2020/5/18 11:56
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/18 11:56     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@ControllerAdvice(basePackages = "com.galaxyt")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 哪些返回值类型需要进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {


        if (methodParameter.hasMethodAnnotation(NotWrapper.class)) {
            return Boolean.FALSE;
        }


        /*
         接口允许直接返回 GlobalResponseWrapper，此处将不再进行额外处理
         */
        /*
        不能够包装字符串类型，否则会因为转换器不匹配导致字符串转换异常
        若必须返回单个字符串，需要使用 new GlobalResponseWrapper().data("");
         */
        final String returnTypeName = methodParameter.getParameterType().getName();  //这里是接收到的返回值的类型


        if (GlobalResponseWrapper.class.getName().equals(returnTypeName)
                || "java.lang.String".equals(returnTypeName)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 在返回之前
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        /*
        若返回值类型为 void 则默认为成功
         */
        final String returnTypeName = methodParameter.getParameterType().getName();  //这里是接收到的返回值的类型
        if ("void".equals(returnTypeName)) {
            return new GlobalResponseWrapper();
        }

        /*
        若返回值不是 json 类型则直接返回
        否则会出现类型转换异常
        接口不能直接返回字符串类型，否则默认的转换器无法进行解析并报错，若必须返回单个字符串，需要使用 new GlobalResponseWrapper().data(""); , 在前面的判断会直接将包装器返回
         */
        if (!mediaType.includes(MediaType.APPLICATION_JSON)) {
            return o;
        }

        if (GlobalExceptionCode.class.getName().equals(returnTypeName)) {
            return new GlobalResponseWrapper((GlobalExceptionCode) o);
        }

        return new GlobalResponseWrapper().data(o);
    }
}
