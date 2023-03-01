package com.timeworx.storage.redis;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/1 9:28 PM
 */
public class RedisKeys {
    /**
     * 用户登陆信息
     * key：timeworx:login:token:{token}
     * type：string
     * value：{userId}
     * ttl：{1h}
     */
    public static final String KEY_TIMEWORX_LOGIN_TOKEN = "timeworx:login:token:%s";
}
