package com.timeworx.common.entity.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/15 9:02 PM
 */
@Data
public class User implements Serializable {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNo;
    // 用户类型 0-用户 1-超管
    private Integer type;
    // 用户状态 0-停用 1-启用
    private Integer status;
    private String introduction;
    // 头像地址
    private String avatar;
}
