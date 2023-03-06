package com.timeworx.modules.security.contoller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.security.service.ShiroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 10:37 PM
 */
@RestController
@Validated
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private ShiroService shiroService;

    @GetMapping("/login")
    @ResponseBody
    public Response<String> login(@NotBlank(message = "email empty") @Email(message = "email incorrect") String email
            ,@NotBlank(message = "password empty") String password) {

        // 查询用户信息
        User user = shiroService.findUserByEmail(email);

        // 比较密码
        if(user == null || !user.getPassword().equals(password)){
            return new Response<>(ReturnCode.PARAM_ERROR,"username or password incorrect!");
        }

        // 登陆成功 生成token
        Response<String> response = shiroService.createToken(user.getId());

        return response;
    }

    @GetMapping("/login/code")
    @ResponseBody
    public Response code(@NotBlank(message = "email empty") @Email(message = "email incorrect") String email){
        // send code
        Response response = shiroService.sendVerifyCode(email);
        return response;
    }

}
