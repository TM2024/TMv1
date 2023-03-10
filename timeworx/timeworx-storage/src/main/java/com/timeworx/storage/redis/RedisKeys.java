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

    /**
     * 验证码生成信息
     * key：timeworx:login:pin:{email}
     * type：string
     * value：{code}
     * ttl：{60s}
     */
    public static final String KEY_TIMEWORX_LOGIN_PIN = "timeworx:login:pin:%s";

    /**
     * ip地址限流
     * key：ip:{ip}:{methodName}
     * type：string
     * value：{count}
     * ttl：{配置}
     */
    public static final String KEY_TIMEWORX_IP_METHOD = "timeworx:ip:%s:%s";


    /**
     * 用户参加活动限制
     * key：timeworx:event:limit:{eventId}
     * type：string
     * value：1
     * ttl：1m
     */
    public static final String KEY_TIMEWORX_EVENT_LIMIT = "timeworx:event:limit:%s";
}
