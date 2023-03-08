package com.timeworx.modules.common.controller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import com.timeworx.modules.common.exception.IpRateLimitException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @Description 全局异常处理
 * @Author: ryzhang
 * @Date 2023/2/3 11:01 PM
 */
@ControllerAdvice
@RestController
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * 权限异常统一处理
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public Response handleAuthorizationException() {
        return new Response(ReturnCode.PERMISSION_DENIED, "您当前没有权限访问~ 请联系管理员");
    }

    /**
     * 参数校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return new Response(ReturnCode.PARAM_ERROR, message);
    }

    /**
     * 参数校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response handler(MethodArgumentNotValidException e){
        String messages = e.getBindingResult().getAllErrors().stream()
                .findFirst().get().getDefaultMessage();
        return new Response(ReturnCode.PARAM_ERROR, messages);
    }


    /**
     * IP限流异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = IpRateLimitException.class)
    public Response handler(IpRateLimitException e){
        return new Response(ReturnCode.PERMISSION_FREQUENT, e.getMessage());
    }


    /**
     * 全局兜底处理
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(HttpServletRequest request, Exception e){
        logger.error("url:{}, 发生了未知错误", request.getRequestURL(), e);
        return new Response(ReturnCode.EXCEPTION, "unknown error");
    }

}
