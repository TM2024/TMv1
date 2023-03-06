package com.timeworx.modules.security.contoller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 11:01 PM
 */
@ControllerAdvice
public class ExceptionController {
    /**
     * 权限异常统一处理
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public String handleAuthorizationException() {
        System.out.println("您当前没有权限访问~ 请联系管理员");
        return "您当前没有权限访问~ 请联系管理员";
    }

    /**
     * 参数校验异常 统一处理
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Response handleConstraintViolationException(
            ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return new Response(ReturnCode.PARAM_ERROR, message);
    }
}
