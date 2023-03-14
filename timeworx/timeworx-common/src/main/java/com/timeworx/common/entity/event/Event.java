package com.timeworx.common.entity.event;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/8 8:21 PM
 */
@Data
public class Event implements Serializable {

    private Long id;

    private Long creatorId;

    private String creatorName;

    private String theme;

    private String desc = "";

    private String photoUrl = "";

    private Date startTime;

    private Date endTime;

    private Integer duration;

    private Integer eventType;

    private String eventLink = "";

    private Double price;

    private Integer limit;

    private String shareLink = "";

    private Integer eventStatus;

    private Date createTime;
}
