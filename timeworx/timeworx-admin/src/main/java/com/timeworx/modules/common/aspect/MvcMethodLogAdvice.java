package com.timeworx.modules.common.aspect;

import com.timeworx.common.utils.JsonUtil;
import com.timeworx.modules.common.exception.IpRateLimitException;
import com.timeworx.storage.redis.RedisKeys;
import com.timeworx.storage.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Description 记录日志输入与输出
 * @Author: ryzhang
 * @Date 2023/3/7 10:44 PM
 */
@Component
@Aspect
public class MvcMethodLogAdvice {
    private static Logger logger = LoggerFactory.getLogger(MvcMethodLogAdvice.class);

    /**
     * 接口日志输入与输出
     * @param joinPoint
     * @param around
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(around)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LogRecordAnnotation around) throws Throwable {
        Object args[] = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // join arguments.
        logger.info("{}.{} param:{} ", method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args, " ; "));
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        logger.info("result:{} duration:{}ms", JsonUtil.encodeString(result), System.currentTimeMillis() - start);
        return result;
    }

    /**
     * IP地址限流
     * @param point
     * @param ipRateLimit
     * @return
     * @throws Throwable
     */
    @Around("@annotation(ipRateLimit)")
    public Object around(ProceedingJoinPoint point, IpRateLimitAnnotation ipRateLimit) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取IP地址
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();

        // 限流
        String key = String.format(RedisKeys.KEY_IP_METHOD, ip, method.getName());
        Long count = RedisUtil.StringOps.incrBy(key, 1);
        if (count == 1) {
            // 设置过期时间
            RedisUtil.KeyOps.expire(key, ipRateLimit.expire(), TimeUnit.SECONDS);
        }
        if (count > ipRateLimit.limit()) {
            logger.warn("ip:{} method:{} count:{} 请求过于频繁!", ip, method.getName(), count);
            throw new IpRateLimitException("请求过于频繁，请稍后再试！");
        }

        return point.proceed();
    }
}
