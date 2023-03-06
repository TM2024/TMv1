package com.timeworx.common.constant;

import java.io.Serializable;

/**
 * 返回码和描述
 * <p>
 * 1. 前两位：返回码大类
 * • 00：成功
 * • 10：权限错误
 * • 20：参数格式
 * • 30：查无数据
 * • 40：数据关系错误
 * • 50：数据冲突
 * • 80：其他失败（非catch类异常）
 * • 99：异常
 * 3. 后两位：具体的返回内容
 * • 01~99
 * 4. 特殊返回码：
 * • 0000：成功
 * • 9999：系统异常
 */
public class ReturnCode implements Serializable {

    /**
     * 0000：成功
     */
    public static final String SUCCESS = "0000";

    /**
     * 20：参数格式
     * 2000: 参数错误
     */
    public static final String PARAM_ERROR = "2000";

    /**
     * 50：数据冲突
     * 5000: 数据已存在
     */
    public static final String DATA_EXIST = "5000";


    /**
     * 9999：异常
     */
    public static final String EXCEPTION = "9999";

}
