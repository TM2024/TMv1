package com.timeworx.modules.event.controller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.DataListResponse;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.common.aspect.LogRecordAnnotation;
import com.timeworx.modules.event.model.req.EventAddOrUpdateReq;
import com.timeworx.modules.event.model.req.EventQryListReq;
import com.timeworx.modules.event.model.vo.EventVo;
import com.timeworx.modules.event.service.EventService;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
     * @param eventAddOrUpdateReq
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ResponseBody
    @LogRecordAnnotation
    public Response addOrUpdate(@RequestBody @Valid EventAddOrUpdateReq eventAddOrUpdateReq){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 用户发布 或修改活动
        Response response = eventService.addOrUpdate(eventAddOrUpdateReq, user);

        return response;
    }

    /**
     * 用户删除活动
     * @param eventId
     * @return
     */
    @GetMapping("/delete")
    @ResponseBody
    @LogRecordAnnotation
    public Response delete(@NotNull(message = "eventId empty") Long eventId){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 用户删除活动
        Response response = eventService.delete(eventId, user);

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
    public DataListResponse<EventVo> qryList(@RequestBody @Valid EventQryListReq qryListDto){

        if(qryListDto.getUserId() == null){
            // 未传userId 用户需登陆
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if(user == null){
                return new DataListResponse(ReturnCode.PERMISSION_DENIED, "user permission denied");
            }
            qryListDto.setUserId(user.getId());
        }
        // 查询用户活动列表
        DataListResponse<EventVo> response = eventService.qryList(qryListDto);
        return response;
    }

    /**
     * 用户参加活动
     * @param eventId
     * @return
     */
    @GetMapping("/join")
    @ResponseBody
    @LogRecordAnnotation
    public Response join(@NotNull(message = "eventId empty") Long eventId){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 参加活动
        Response response = eventService.join(eventId, user);

        return response;
    }

    /**
     * 用户退出活动
     * @param eventId
     * @return
     */
    @GetMapping("/exit")
    @ResponseBody
    @LogRecordAnnotation
    public Response exit(@NotNull(message = "eventId empty") Long eventId){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 参加活动
        Response response = eventService.exit(eventId, user);

        return response;
    }
}
