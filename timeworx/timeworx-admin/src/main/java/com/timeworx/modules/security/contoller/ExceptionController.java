package com.timeworx.modules.security.contoller;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 11:01 PM
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = AuthorizationException.class)
    public String handleAuthorizationException() {

        System.out.println("您当前没有权限访问~ 请联系管理员");
        return "您当前没有权限访问~ 请联系管理员";
    }
}
