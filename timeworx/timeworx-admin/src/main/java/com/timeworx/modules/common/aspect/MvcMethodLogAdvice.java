package com.timeworx.modules.common.aspect;

import com.timeworx.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description 记录日志输入与输出
 * @Author: ryzhang
 * @Date 2023/3/7 10:44 PM
 */
@Component
@Aspect
public class MvcMethodLogAdvice {
    private static Logger log = LoggerFactory.getLogger(MvcMethodLogAdvice.class);

    @Around(value = "@annotation(around)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LoginAnnotation around) throws Throwable {
        Object args[] = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // join arguments.
        log.info("{}.{} param:{} ", method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args, " ; "));
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        log.info("result:{} duration:{}ms", JsonUtil.encodeString(result), System.currentTimeMillis() - start);
        return result;
    }
}
