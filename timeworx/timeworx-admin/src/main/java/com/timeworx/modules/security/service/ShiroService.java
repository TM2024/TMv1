package com.timeworx.modules.security.service;


import com.timeworx.common.entity.user.User;
import com.timeworx.storage.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:01 PM
 */
@Service
public class ShiroService {

    @Resource
    private UserMapper userMapper;

    public User findUserName(String username) {
        //TODO 数据库查询
//        User user = userMapper.findUserByName(username);
        User user = new User();
        if(username.equals("admin")) {
            user.setId(1l);
            user.setName("admin");
            user.setPassword("123");
            user.setType(1);
        }else {
            user.setId(2l);
            user.setName("admin1");
            user.setPassword("456");
            user.setType(0);
        }
        user.setStatus(1);
        return user;
    }

    public String createToken(Long userId) {
        //TODO 生成token 并记录到Redis中
//        TokenGenerator.generateValue();
        if(userId == 1) {
            return "1";
        }else {
            return "2";
        }
    }

    public String getUserByToken(String token) {
        // TODO 从Redis中获取用户Id
        if(token.equals("1")) {
            return "admin";
        }else {
            return "admin1";
        }
    }

    public Set<String> getUserPermissions(String username) {
        // TODO 从数据库中获取权限信息
        if(username.equals("admin")) {
            return new HashSet<String>() {
                {
                    add("p:admin");
                }
            };
        }else {
            return new HashSet<String>() {
                {
                    add("p:user");
                }
            };
        }
    }
}
