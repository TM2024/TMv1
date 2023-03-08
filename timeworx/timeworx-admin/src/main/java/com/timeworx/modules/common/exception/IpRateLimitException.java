package com.timeworx.modules.common.exception;

/**
 * @Description 限流异常
 * @Author: ryzhang
 * @Date 2023/3/8 8:16 PM
 */
public class IpRateLimitException extends RuntimeException {

    public IpRateLimitException(String message) {
        super(message);
    }
}
