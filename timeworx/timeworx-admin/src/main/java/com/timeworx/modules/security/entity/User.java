package com.timeworx.modules.security.entity;

import lombok.Data;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 10:32 PM
 */
@Data
public class User {
    public Integer id;
    public String username;
    public String password;
    public String role;
    public String state;
}
