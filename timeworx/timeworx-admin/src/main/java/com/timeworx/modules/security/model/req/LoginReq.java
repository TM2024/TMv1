package com.timeworx.modules.security.model.req;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/7 10:59 PM
 */
@Data
public class LoginReq {
    @NotBlank(message = "email empty")
    @Email(message = "email incorrect")
    private String email;

    @NotBlank(message = "password empty")
    private String password;
}
