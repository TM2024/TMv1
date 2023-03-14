package com.timeworx.common.constant.event;

/**
 * @Description 活动订单状态
 * @Author: ryzhang
 * @Date 2023/3/14 11:30 PM
 */
public class EventOrderStatus {
    /**
     * 订单未付款
     */
    public static final int UNCOMMITTED = -1;
    /**
     * 订单未付款
     */
    public static final int UNPAID = 0;
    /**
     * 订单已付款
     */
    public static final int PAID = 1;
    /**
     * 订单已完成
     */
    public static final int FINISH = 2;
    /**
     * 订单已取消
     */
    public static final int CANCELED = 3;
    /**
     * 订单退款中
     */
    public static final int REFUNDING = 4;
    /**
     * 订单已退款
     */
    public static final int REFUNDED = 5;
}
