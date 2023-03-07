package com.timeworx.modules.security.service;

import com.timeworx.common.constant.ReturnCode;
import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.common.utils.UniqueIDUtil;
import com.timeworx.modules.security.contoller.LoginController;
import com.timeworx.modules.security.dto.RegisterDto;
import com.timeworx.modules.security.oauth2.TokenGenerator;
import com.timeworx.modules.security.proxy.EmailProxy;
import com.timeworx.storage.mapper.user.UserMapper;
import com.timeworx.storage.redis.RedisKeys;
import com.timeworx.storage.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:01 PM
 */
@Service
public class ShiroService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailProxy emailProxy;

    public User findUserByEmail(String email) {
        User user = userMapper.findUserByEmail(email);
        return user;
    }

    public User findUserById(Long userId) {
        User user = userMapper.findUserById(userId);
        return user;
    }

    public Response<String> createToken(Long userId) {
        // 生成token
        Response<String> response = TokenGenerator.generateValue();

        if(response.isSuccess() && StringUtils.isNotBlank(response.getData())){
            String token = response.getData();
            // 将token记录到Redis中
            RedisUtil.StringOps.set(String.format(RedisKeys.KEY_TIMEWORX_LOGIN_TOKEN, token), String.valueOf(userId));
            // 更新过期时间
            RedisUtil.KeyOps.expire(String.format(RedisKeys.KEY_TIMEWORX_LOGIN_TOKEN, token), 1, TimeUnit.HOURS);
        }
        return response;
    }

    public String getUserByToken(String token) {
        String userId = RedisUtil.StringOps.get(String.format(RedisKeys.KEY_TIMEWORX_LOGIN_TOKEN, token));
        return userId;
    }

    /**
     * send Email
     * @param email
     */
    public Response sendVerifyCode(String email){

        // 验证码是否已生成
        String code = generateRandomNumber();

        // 验证邮箱是否已发送过验证码
        boolean result = RedisUtil.StringOps.setIfAbsent(String.format(RedisKeys.KEY_TIMEWORX_LOGIN_PIN, email), code, 60, TimeUnit.SECONDS);

        if(!result){
            // 验证码已生成
            return new Response(ReturnCode.DATA_EXIST, "verify code has send");
        }

        // 验证邮箱是否注册
        User user = userMapper.findUserByEmail(email);

        if(user != null){
            // 邮箱已注册
            return new Response(ReturnCode.DATA_EXIST, "email has register");
        }

        // 发送验证码
        Response response = emailProxy.sendVerifyCode(email, code);

        return response;
    }

    /**
     * 生成6位随机验证码
     * @return
     */
    public String generateRandomNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }

    /**
     * user register
     * @param registerDto
     * @return
     */
    public Response register(RegisterDto registerDto) {
        // 获取验证码
        String verifyCode = RedisUtil.StringOps.get(String.format(RedisKeys.KEY_TIMEWORX_LOGIN_PIN, registerDto.getEmail()));

        if(StringUtils.isBlank(verifyCode)){
            // 验证码已过期
            return new Response(ReturnCode.DATA_NOT_EXIST, "code has expire");

        }else if(!verifyCode.equals(registerDto.getPin())){
            // 验证码不正确
            return new Response(ReturnCode.DATA_ERROR, "pin incorrect");
        }

        // 验证邮箱是否已注册
        // 验证邮箱是否注册
        User user = userMapper.findUserByEmail(registerDto.getEmail());

        if(user != null){
            // 邮箱已注册
            return new Response(ReturnCode.DATA_EXIST, "email has register");
        }

        // 用户注册
        User register = new User();
        Long id = UniqueIDUtil.generateId();
        register.setId(id);
        // 随机生成用户名
        register.setName("user" + id );
        register.setEmail(registerDto.getEmail());
        // TODO 用户密码需要加密
        register.setPassword(registerDto.getPassword());

        userMapper.insertUser(register);
        return new Response(ReturnCode.SUCCESS, "success");

    }

    public Set<String> getUserPermissions(String username) {
        // 权限暂为空
        return new HashSet<String>() {
            {
                add("p:user");
            }
        };
    }
}
