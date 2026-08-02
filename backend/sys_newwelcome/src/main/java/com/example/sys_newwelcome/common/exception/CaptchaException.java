package com.example.sys_newwelcome.common.exception;
import org.springframework.security.core.AuthenticationException;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.common.exception.CaptchaException
 * @version:1.0
 */

/**
 * 验证码错误异常
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg){
        /**
         * 传递错误信息
         */
        super(msg);
    }
}
