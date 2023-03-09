package com.timeworx.common.entity.base;

import lombok.Data;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/9 10:12 PM
 */
@Data
public class BasePage {

    private int pageIndex = 0;

    private int pageSize = 10;

    private int pageStart;

    public int getPageStart() {
        pageStart = (pageIndex - 1) * pageSize;
        return pageStart;
    }
}
