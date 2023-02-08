package com.timeworx.modules.security.contoller;

import com.timeworx.common.entity.Response;
import com.timeworx.modules.security.entity.User;
import com.timeworx.modules.security.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 10:37 PM
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private ShiroService shiroService;

    @GetMapping("/login")
    @ResponseBody
    public Response<String> login(String username, String password) {
        // 校验用户名
        if(StringUtils.isBlank(username)){
            logger.warn("method:login, username empty!");
            return new Response<>("1", "用户名为空");
        }

        User user = shiroService.findUserName(username);

        // 比较密码
        if(user == null || !user.getPassword().equals(password)){
            return new Response<>("1","用户名和密码不正确");
        }

        // 登陆成功 生成token
        String token = shiroService.createToken(user.getId());

        return new Response<>("0", "success", token);
    }
}
