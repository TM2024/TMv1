package com.timeworx.common.entity.event;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/10 7:18 PM
 */
@Data
public class EventOrder implements Serializable {
    private Long id;

    private Long eventId;

    private Long purchaserId;

    private String purchaserName;

    private Integer orderStatus;

    private Date createTime;
}
