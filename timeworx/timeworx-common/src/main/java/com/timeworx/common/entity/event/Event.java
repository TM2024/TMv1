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

    public class EventStatus{
        /**
         * 活动未开始
         */
        public static final int WAITING = 0;
        /**
         * 活动进行中
         */
        public static final int ONGOING = 1;
        /**
         * 活动已完成
         */
        public static final int FINISH = 2;
        /**
         * 活动取消中
         */
        public static final int CANCELING = 3;
        /**
         * 活动已取消
         */
        public static final int DELETED = 4;
    }
}
