package com.timeworx.common.constant.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 活动订单场景
 * @Author: ryzhang
 * @Date 2023/3/14 11:30 PM
 */
public class EventOrderScene {

    /**
     * 参加活动订单状态 0-未付款 1-已付款
     */
    public static final List<Integer> PARTICIPATED_STATUS = new ArrayList<Integer>(){
        {
            add(EventOrderStatus.UNPAID);
            add(EventOrderStatus.PAID);
        }
    };

    /**
     * 未结束订单状态 0-未付款 1-已付款 4-退款中
     */
    public static final List<Integer> UNCLOSED_STATUS = new ArrayList<Integer>(){
        {
            add(EventOrderStatus.UNPAID);
            add(EventOrderStatus.PAID);
            add(EventOrderStatus.REFUNDING);
        }
    };
}
