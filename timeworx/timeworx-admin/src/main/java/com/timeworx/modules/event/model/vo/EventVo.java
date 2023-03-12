package com.timeworx.modules.event.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 活动列表展示
 * @Author: ryzhang
 * @Date 2023/3/11 10:47 AM
 */
@Data
public class EventVo implements Serializable {

    private Long id;

    private String creatorName;

    private String theme;

    private String desc;

    private Date startTime;

    private Date endTime;

    private Integer duration;

    private Integer eventType;

    private Double price;

    private Integer eventStatus;

    private Integer limit;

    /**
     * 已参与人数 未付款 + 已付款
     */
    private Integer participatedNum;

}
