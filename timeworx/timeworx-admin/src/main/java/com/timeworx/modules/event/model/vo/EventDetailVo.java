package com.timeworx.modules.event.model.vo;

import com.timeworx.common.entity.event.Event;
import lombok.Data;

/**
 * @Description 活动详情展示
 * @Author: ryzhang
 * @Date 2023/3/11 10:47 AM
 */
@Data
public class EventDetailVo extends Event {

    /**
     * 已参与人数 未付款 + 已付款
     */
    private Integer participatedNum;

    /**
     * 用户最近订单状态 -1-未参加 0-未付款 1-已付款 3-退款中
     */
    private Integer lastOrderStatus = -1;

}
