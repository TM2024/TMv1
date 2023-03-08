package com.timeworx.modules.event.controller;

import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.common.aspect.LogRecordAnnotation;
import com.timeworx.modules.event.dto.AddOrUpdateDto;
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
     * @param addOrUpdateDto
     * @return
     */
    @PostMapping("/addOrUpdate")
    @ResponseBody
    @LogRecordAnnotation
    public Response addOrUpdate(@RequestBody @Valid AddOrUpdateDto addOrUpdateDto){
        // 获取用户登陆信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 用户发布 或修改活动
        Response response = eventService.addOrUpdate(addOrUpdateDto, user);

        return response;
    }

}
