package com.timeworx.common.constant.event;

/**
 * @Description 活动状态
 * @Author: ryzhang
 * @Date 2023/3/14 11:30 PM
 */
public class EventStatus {
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
