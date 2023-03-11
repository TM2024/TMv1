package com.timeworx.modules.event.model.req;

import com.timeworx.common.entity.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/9 9:27 PM
 */
@Data
public class EventQryListReq extends BasePage {

    private Long userId;

    @NotNull(message = "status empty")
    private Integer status;
}
