package com.example.sys_newwelcome.common.annotation;


import java.lang.annotation.*;

/**
 * 自定义日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HoneyLogs {
    /**
     * 操作模块
     */
    String operation();

    /**
     * 操作类型
     */
    String type();
    /**
     * URL
     */
    String url();
}
