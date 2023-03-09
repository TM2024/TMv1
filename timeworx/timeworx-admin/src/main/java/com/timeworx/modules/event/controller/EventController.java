package com.timeworx.modules.event.controller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.DataListResponse;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.event.Event;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.common.aspect.LogRecordAnnotation;
import com.timeworx.modules.event.dto.EventAddOrUpdateDto;
import com.timeworx.modules.event.dto.EventQryListDto;
import com.timeworx.modules.event.service.EventService;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/7 9:46 PM
 */
@RestController
@RequestMapping(value = "event")
@Validated
public class EventController {

    @Resource
    private EventService eventService;

    /**
     * 用户发布或修改活动
     * @param eventAddOrUpdateDto
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ResponseBody
    @LogRecordAnnotation
    public Response addOrUpdate(@RequestBody @Valid EventAddOrUpdateDto eventAddOrUpdateDto){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 用户发布 或修改活动
        Response response = eventService.addOrUpdate(eventAddOrUpdateDto, user);

        return response;
    }

    /**
     * 查询用户活动列表
     * @param qryListDto
     * @return
     */
    @PostMapping("/qryList")
    @ResponseBody
    @LogRecordAnnotation
    public DataListResponse<Event> qryList(@RequestBody @Valid EventQryListDto qryListDto){

        if(qryListDto.getUserId() == null){
            // 未传userId 用户需登陆
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if(user == null){
                return new DataListResponse(ReturnCode.PERMISSION_DENIED, "user permission denied");
            }
            qryListDto.setUserId(user.getId());
        }
        // 查询用户活动列表
        DataListResponse<Event> response = eventService.qryList(qryListDto);
        return response;
    }
}
