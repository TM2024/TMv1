package com.timeworx.modules.security.contoller;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.common.aspect.IpRateLimitAnnotation;
import com.timeworx.modules.common.aspect.LogRecordAnnotation;
import com.timeworx.modules.security.dto.LoginDto;
import com.timeworx.modules.security.dto.RegisterDto;
import com.timeworx.modules.security.service.ShiroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    /**
     * 用户登陆
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    @LogRecordAnnotation
    // ip地址限流
    @IpRateLimitAnnotation
    public Response<String> login(@Valid @RequestBody LoginDto loginDto) {
        // 查询用户信息
        User user = shiroService.findUserByEmail(loginDto.getEmail());

        // 比较密码
        if(user == null || !user.getPassword().equals(loginDto.getPassword())){
            return new Response<>(ReturnCode.PARAM_ERROR,"username or password incorrect!");
        }

        // 登陆成功 生成token
        Response<String> response = shiroService.createToken(user.getId());
        return response;
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    @GetMapping("/login/code")
    @ResponseBody
    @LogRecordAnnotation
    public Response code(@NotBlank(message = "email empty") @Email(message = "email incorrect") String email){
        // send code
        Response response = shiroService.sendVerifyCode(email);
        return response;
    }


    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @PostMapping("/login/register")
    @ResponseBody
    @LogRecordAnnotation
    public Response register(@Valid @RequestBody RegisterDto registerDto){
        // register
        Response response = shiroService.register(registerDto);
        return response;
    }
}
