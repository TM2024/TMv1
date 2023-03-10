package com.timeworx.modules.event.service;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.DataListResponse;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.event.Event;
import com.timeworx.common.entity.event.EventOrder;
import com.timeworx.common.entity.user.User;
import com.timeworx.common.utils.UniqueIDUtil;
import com.timeworx.modules.event.dto.EventAddOrUpdateDto;
import com.timeworx.modules.event.dto.EventQryListDto;
import com.timeworx.storage.mapper.event.EventMapper;
import com.timeworx.storage.redis.RedisKeys;
import com.timeworx.storage.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/7 9:47 PM
 */
@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Resource
    private EventMapper eventMapper;

    public Response addOrUpdate(EventAddOrUpdateDto eventAddOrUpdateDto, User user) {

        if(eventAddOrUpdateDto.getEventId() != null){
            // 用户修改活动 获取活动信息
            Event event = eventMapper.qryEventById(eventAddOrUpdateDto.getEventId());

            // 订单不属于当前用户
            if(event.getCreatorId().longValue() != user.getId().longValue()){
                return new Response(ReturnCode.PERMISSION_DENIED, "user permission denied");
            }

            // 订单状态不是未开始 无法修改
            if(event.getEventStatus() != Event.EventStatus.WAITING){
                return new Response(ReturnCode.EVENT_MODIFY_DENIED, "event can not modify");
            }

            // 价格无法修改
            if(eventAddOrUpdateDto.getPrice().doubleValue() != event.getPrice().doubleValue()){
                return new Response(ReturnCode.EVENT_PRICE_MODIFY_DENIED, "price can not modify");
            }

            // 人数限制不得低于已购人数 包含未付款和已付款
            Integer count = eventMapper.qryOrderCountByEventId(eventAddOrUpdateDto.getEventId());
            if(count > eventAddOrUpdateDto.getLimit()){
                return new Response(ReturnCode.EVENT_NUMBER_MODIFY_DENIED, "The number of subscribers exceeds the limit");
            }

            // 活动日期不可修改
            if(eventAddOrUpdateDto.getStartTime().getTime() != event.getStartTime().getTime()
                    || eventAddOrUpdateDto.getEndTime().getTime() != event.getEndTime().getTime()){
                return new Response(ReturnCode.EVENT_TIME_MODIFY_DENIED, "event time can not modify");
            }

            // 活动更新
            event.setTheme(eventAddOrUpdateDto.getTheme());
            event.setDesc(eventAddOrUpdateDto.getDesc());
            event.setPhotoUrl(eventAddOrUpdateDto.getPhotoUrl());
            event.setEventType(eventAddOrUpdateDto.getEventType());
            event.setEventLink(eventAddOrUpdateDto.getEventLink());
            event.setLimit(eventAddOrUpdateDto.getLimit());
            // 活动写入
            eventMapper.replaceEvent(event);
            return new Response(ReturnCode.SUCCESS, "update success");
        }

        // 计算活动时长
        long diff = eventAddOrUpdateDto.getEndTime().getTime() - eventAddOrUpdateDto.getStartTime().getTime();
        if(diff < 0l){
            // 开始时间大于结束时间
            return new Response(ReturnCode.PARAM_ERROR, "startTime is later then endTime");
        }
        // 转换成小时
        Integer duration = (int) diff / 3600000;

        // 用户购买
        Event event = new Event();
        BeanUtils.copyProperties(eventAddOrUpdateDto, event);
        event.setId(UniqueIDUtil.generateId());
        // 用户信息
        event.setCreatorId(user.getId());
        event.setCreatorName(user.getName());
        // 时长
        event.setDuration(duration);
        // 活动状态
        event.setEventStatus(Event.EventStatus.WAITING);
        // 创建时间
        event.setCreateTime(new Date());
        // 活动写入
        eventMapper.replaceEvent(event);
        return new Response(ReturnCode.SUCCESS, "insert success");
    }

    public DataListResponse<Event> qryList(EventQryListDto qryListDto) {

        List<Event> list = eventMapper.qryEventList(qryListDto.getUserId(), qryListDto.getStatus()
                , qryListDto.getPageStart(), qryListDto.getPageIndex(), qryListDto.getPageSize());

        Long count = eventMapper.qryEventListCount(qryListDto.getUserId(), qryListDto.getStatus());

        return new DataListResponse<>(ReturnCode.SUCCESS, "success", list, count);
    }

    /**
     * 用户参加活动
     * @param eventId
     * @param user
     * @return
     */
    public Response join(Long eventId, User user) {
        // 查询用户是否已经下单 0-未付款 1-已付款
        EventOrder order = eventMapper.qryUserEventOrder(eventId, user.getId());
        if(order != null){
            // 用户已下单
            return new Response(ReturnCode.EVENT_HAS_JOIN, "event has join");
        }

        // 锁定活动 当前仅允许一个用户参加
        boolean result = RedisUtil.StringOps.setIfAbsent(String.format(RedisKeys.KEY_TIMEWORX_EVENT_LIMIT, eventId), "1", 60, TimeUnit.SECONDS);
        if(!result){
            // 活动锁定失败
            return new Response(ReturnCode.EVENT_JOIN_FAILED, "join event failed");
        }
        // 获取用户活动信息
        Event event = eventMapper.qryEventById(eventId);

        // 人数限制不得低于已购人数 包含未付款和已付款
        Integer count = eventMapper.qryOrderCountByEventId(eventId);

        if(event.getLimit() <= count){
            // 参加活动人数已满 解锁
            RedisUtil.KeyOps.delete(String.format(RedisKeys.KEY_TIMEWORX_EVENT_LIMIT, eventId));
            return new Response(ReturnCode.EVENT_JOIN_LIMIT, "the number of event has reached the maximum limit");
        }

        // 新增订单信息
        EventOrder eventOrder = new EventOrder();
        eventOrder.setId(UniqueIDUtil.generateId());
        eventOrder.setEventId(eventId);
        eventOrder.setPurchaserId(user.getId());
        eventOrder.setPurchaserName(user.getName());
        eventOrder.setOrderStatus(EventOrder.EventOrderStatus.UNPAID);
        eventOrder.setCreateTime(new Date());
        eventMapper.insertEventOrder(eventOrder);

        // 解锁
        RedisUtil.KeyOps.delete(String.format(RedisKeys.KEY_TIMEWORX_EVENT_LIMIT, eventId));
        return new Response(ReturnCode.SUCCESS, "success");
    }

    public Response exit(Long eventId, User user) {
        // 查询订单详情 0-未付款 1-已付款
        EventOrder eventOrder = eventMapper.qryUserEventOrder(eventId, user.getId());
        if(eventOrder == null){
            // 用户当前未参加活动
            return new Response(ReturnCode.EVENT_EXIT_FAILED, "user has no event order");
        }

        if(EventOrder.EventOrderStatus.UNPAID == eventOrder.getOrderStatus()){
            // 用户未付款 改为 已取消
            eventMapper.updateEventOrderStatus(eventOrder.getId(), EventOrder.EventOrderStatus.CANCELED);
        }else {
            // 用户已付款 改为 退款中
            eventMapper.updateEventOrderStatus(eventOrder.getId(), EventOrder.EventOrderStatus.REFUNDING);
        }
        return new Response(ReturnCode.SUCCESS, "success");
    }
}
