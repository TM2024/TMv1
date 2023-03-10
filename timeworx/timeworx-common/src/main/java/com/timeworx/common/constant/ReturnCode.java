package com.timeworx.common.constant;

import java.io.Serializable;

/**
 * 返回码和描述
 * <p>
 * 1. 前两位：返回码大类
 * • 00：成功
 * • 10：权限错误
 * • 20：参数格式
 * • 30：用户模块
 * • 40：活动模块
 * • 99：异常
 * 2. 后两位：具体的返回内容
 * • 01~99
 * 3. 特殊返回码：
 * • 0000：成功
 * • 9999：系统异常
 */
public class ReturnCode implements Serializable {

    /**
     * 0000：成功
     */
    public static final String SUCCESS = "0000";

    /**
     * 10：权限错误
     * 1000: 无权访问
     * 1001: 访问频繁
     * 1002: 验证码过期
     * 1003: 验证码错误
     * 1004: 验证码已发送
     * 1005: 邮箱已注册
     */
    public static final String PERMISSION_DENIED = "1000";
    public static final String PERMISSION_FREQUENT = "1001";
    public static final String CODE_EXPIRE = "1002";
    public static final String CODE_ERROR = "1003";
    public static final String CODE_SENT = "1004";
    public static final String EMAIL_REGISTER = "1005";

    /**
     * 20：参数格式
     * 2000: 参数错误
     */
    public static final String PARAM_ERROR = "2000";

    /**
     * 30：用户模块
     */


    /**
     * 40：活动模块
     * 4000: 活动无法修改
     * 4001: 活动价格无法修改
     * 4002: 活动人数无法修改
     * 4003: 活动时间无法修改
     * 4004: 活动参加限制
     * 4005：活动参加失败
     * 4006: 活动已参加
     * 4007: 活动退出失败
     */
    public static final String EVENT_MODIFY_DENIED = "4000";
    public static final String EVENT_PRICE_MODIFY_DENIED  = "4001";
    public static final String EVENT_NUMBER_MODIFY_DENIED  = "4002";
    public static final String EVENT_TIME_MODIFY_DENIED  = "4003";
    public static final String EVENT_JOIN_LIMIT = "4004";
    public static final String EVENT_JOIN_FAILED = "4005";
    public static final String EVENT_HAS_JOIN = "4006";
    public static final String EVENT_EXIT_FAILED= "4007";


    /**
     * 9999：异常
     */
    public static final String EXCEPTION = "9999";

}
