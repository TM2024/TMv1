package com.timeworx.modules.security.service;


import com.timeworx.common.entity.base.Response;
import com.timeworx.common.entity.user.User;
import com.timeworx.modules.security.oauth2.TokenGenerator;
import com.timeworx.storage.mapper.user.UserMapper;
import com.timeworx.storage.redis.RedisKeys;
import com.timeworx.storage.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:01 PM
 */
@Service
public class ShiroService {

    @Resource
    private UserMapper userMapper;

    public User findUserByName(String username) {
        User user = userMapper.findUserByName(username);
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

    public Set<String> getUserPermissions(String username) {
        // 权限暂为空
        return new HashSet<String>() {
            {
                add("p:user");
            }
        };
    }
}
