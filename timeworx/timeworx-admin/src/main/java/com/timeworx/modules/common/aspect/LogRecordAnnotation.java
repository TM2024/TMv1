package com.timeworx.modules.common.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 日志记录注解 记录接口的输入和输出
 * @Author: ryzhang
 * @Date 2023/3/7 10:51 PM
 */
@Retention(RetentionPolicy.RUNTIME)//运行时有效
@Target(ElementType.METHOD)//作用于方法
public @interface LogRecordAnnotation {
}
