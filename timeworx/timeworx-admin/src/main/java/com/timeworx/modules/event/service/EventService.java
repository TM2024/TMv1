package com.timeworx.modules.event.service;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.DataListResponse;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.event.Event;
import com.timeworx.common.entity.user.User;
import com.timeworx.common.utils.UniqueIDUtil;
import com.timeworx.modules.event.dto.EventAddOrUpdateDto;
import com.timeworx.modules.event.dto.EventQryListDto;
import com.timeworx.storage.mapper.event.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
                return new Response(ReturnCode.DATA_ERROR, "event can not modify");
            }

            // 价格无法修改
            if(eventAddOrUpdateDto.getPrice().doubleValue() != event.getPrice().doubleValue()){
                return new Response(ReturnCode.DATA_ERROR, "price can not modify");
            }

            // 人数限制不得低于已购人数 包含未付款和已付款
            Integer count = eventMapper.qryOrderCountByEventId(eventAddOrUpdateDto.getEventId());
            if(count > eventAddOrUpdateDto.getLimit()){
                return new Response(ReturnCode.DATA_ERROR, "The number of subscribers exceeds the limit");
            }

            // 活动日期不可修改
            if(eventAddOrUpdateDto.getStartTime().getTime() != event.getStartTime().getTime()
                    || eventAddOrUpdateDto.getEndTime().getTime() != event.getEndTime().getTime()){
                return new Response(ReturnCode.DATA_ERROR, "event time can not modify");
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
}
