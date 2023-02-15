package com.timeworx.common.entity.base;

import lombok.Data;

import java.util.Collection;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:11 PM
 */
@Data
public class DataListResponse extends Response<Collection>{

    private long total;

    public DataListResponse() {
        super();
    }

    public DataListResponse(String code, String desc) {
        super(code, desc);
    }

    public DataListResponse(String code, String desc, Collection data) {
        super(code, desc, data);
    }
}
