package com.timeworx.modules.security.entity;

import lombok.Data;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 10:32 PM
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String state;
}
