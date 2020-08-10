package com.galaxyt.normae.web.handler;

import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.core.exception.GlobalException;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常拦截
 * @author zhouqi
 * @date 2020/4/10 15:33
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
 ---------------------------------------------------------------------------------*
 * 2020/4/10 15:33     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 判断该接口的方法是否需要被进场拦截处理
     * @param request
     * @return
     */
    private boolean methodNotWrapper(HttpServletRequest request) {
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
        return handlerMethod.hasMethodAnnotation(NotWrapper.class);
    }



    /**
     * 自定义异常处理
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public GlobalResponseWrapper customerExceptionHandler(HttpServletRequest request, GlobalException exception) throws Exception {

        if (this.methodNotWrapper(request)) {
            throw exception;
        }

        log.info("拦截到抛出的自定义异常", exception);
        return new GlobalResponseWrapper(exception.getCode()).msg(exception.getMessage());
    }

    /**
     * 注解参数异常处理
     * 将全部的异常参数拼接返回
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GlobalResponseWrapper methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception) throws Exception {

        if (this.methodNotWrapper(request)) {
            throw exception;
        }

        //获取全部异常
        BindingResult bindingResult = exception.getBindingResult();
        //获取第一条异常进行返回
        String message = bindingResult.getAllErrors().get(0).getDefaultMessage();

        log.info("拦截到请求参数异常", exception);
        return new GlobalResponseWrapper(GlobalExceptionCode.REQUEST_ARGUMENT_EXCEPTION).msg(message);
    }

    /**
     * 处理集合中的注解异常
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public GlobalResponseWrapper constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException exception) throws Exception {
        if (this.methodNotWrapper(request)) {
            throw exception;
        }

        String message = exception.getConstraintViolations().iterator().next().getMessage();
        log.info("拦截到请求参数异常",exception);
        return new GlobalResponseWrapper(GlobalExceptionCode.REQUEST_ARGUMENT_EXCEPTION).msg(message);

    }


    /**
     * 其他的异常拦截处理
     * 不建议放开这些异常拦截，因为这些拦截无法完全掌控，且其中会包含一些其他不确定因素
     * 拦截这些异常会影响到熔断器的使用
     * 而且如果某些异常程序员无法捕获 , 那么就应该抛出到前端让前端开发人员进行统一处理
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    /*@ExceptionHandler(Exception.class)
    @ResponseBody*/
    public GlobalResponseWrapper defaultExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {

        if (this.methodNotWrapper(request)) {
            throw exception;
        }

        if (exception instanceof org.springframework.web.servlet.NoHandlerFoundException) { //404处理
            log.warn("拦截到 404 异常",exception);
        } else {    //其它全部为 500 异常
            log.error("拦截到服务器内部异常",exception);
        }
        return new GlobalResponseWrapper(GlobalExceptionCode.ERROR).msg(exception.getMessage());
    }


}
