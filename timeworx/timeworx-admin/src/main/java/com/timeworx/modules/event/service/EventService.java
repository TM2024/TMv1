package com.timeworx.modules.event.service;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.event.Event;
import com.timeworx.common.entity.user.User;
import com.timeworx.common.utils.UniqueIDUtil;
import com.timeworx.modules.event.dto.AddOrUpdateDto;
import com.timeworx.storage.mapper.event.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    public Response addOrUpdate(AddOrUpdateDto addOrUpdateDto, User user) {

        // TODO 计算活动时长
        Integer duration = 0;

        if(addOrUpdateDto.getEventId() != null){
            // 用户修改活动 获取活动信息
            Event event = eventMapper.qryEventById(addOrUpdateDto.getEventId());

            // 订单不属于当前用户
            if(event.getCreatorId() != user.getId()){
                return new Response(ReturnCode.PERMISSION_DENIED, "user permission denied");
            }

            // 订单状态不是未开始 无法修改
            if(event.getEventStatus() != Event.EventStatus.WAITING){
                return new Response(ReturnCode.DATA_ERROR, "event can not modify");
            }

            // 价格无法修改
            if(addOrUpdateDto.getPrice() != event.getPrice()){
                return new Response(ReturnCode.DATA_ERROR, "price can not modify");
            }

            // 人数限制不得低于已购人数 包含未付款和已付款
            Integer count = eventMapper.qryOrderCountByEventId(addOrUpdateDto.getEventId());
            if(count > addOrUpdateDto.getLimit()){
                return new Response(ReturnCode.DATA_ERROR, "The number of subscribers exceeds the limit");
            }
            // TODO
        }

        // 用户购买
        Event event = new Event();
        BeanUtils.copyProperties(addOrUpdateDto, event);
        event.setId(UniqueIDUtil.generateId());
        // 用户信息
        event.setCreatorId(user.getId());
        event.setCreatorName(user.getName());
        // 时长
        event.setDuration(duration);
        // 活动状态
        event.setEventStatus(Event.EventStatus.WAITING);
        // TODO

        return new Response(ReturnCode.SUCCESS, "success");
    }
}
