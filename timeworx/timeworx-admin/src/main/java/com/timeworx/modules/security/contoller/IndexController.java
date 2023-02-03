package com.timeworx.modules.security.contoller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 10:36 PM
 */
@RestController
public class IndexController {
    @RequiresPermissions("p:user")
    @RequestMapping("/index")
    public String index() {
        // 登录成后，即可通过Subject获取登录的用户信息
        return "index";
    }
}
