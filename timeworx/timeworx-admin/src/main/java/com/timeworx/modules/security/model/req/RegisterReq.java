package com.timeworx.modules.security.model.req;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/7 9:50 PM
 */
@Data
public class RegisterReq {

    @NotBlank(message = "email empty")
    @Email(message = "email incorrect")
    private String email;

    @NotBlank(message = "password empty")
    private String password;

    @NotBlank(message = "pin empty")
    @Size(min = 6, max = 6, message = "pin length error")
    private String pin;
}
