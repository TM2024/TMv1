package com.timeworx.common.entity.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:06 PM
 */
@Data
public class Response<T> implements Serializable {

    private String code;

    private String desc;

    private T data;

    public Response() {
    }

    public Response(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Response(String code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }
}
