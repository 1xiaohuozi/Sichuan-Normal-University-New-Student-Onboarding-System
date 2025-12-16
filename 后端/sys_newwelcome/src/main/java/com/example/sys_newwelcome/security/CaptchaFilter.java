package com.example.sys_newwelcome.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sys_newwelcome.common.exception.CaptchaException;
import com.example.sys_newwelcome.common.lang.Const;
import com.example.sys_newwelcome.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 */

/**
 * OncePerRequestFilter
 * 因为验证码只用执行一次所以继承这个类
 * 确保每个HTTP请求中的过滤器逻辑只执行一次
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    /**
     * FilterChain表示过滤器链，用于将请求传递到下一个过滤器或请求处理器
     */
    /**
     * doFilterInternal是过滤器的核心方法，编写具体逻辑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String url = request.getRequestURI();
        /**
         * 匹配是否是登录（/login）请求且是POST请求
         */
        if("/login".equals(url) && request.getMethod().equals("POST")){
            try{
                /**
                 * 校验验证码
                 */
                validate(request);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        /**
         * 无论校验成功或者失败都通过filterChain.doFilter将请求传递给下一个过滤器或请求处理器
         * 以继续处理请求
         */
        filterChain.doFilter(request,response);
    }

    /**
     * 校验逻辑
     */
    private void validate(HttpServletRequest request){
        /**
         * 分别获取用户请求输入地验证码和验证码令牌（token）
         */
        String code = request.getParameter("code");
        String key = request.getParameter("token");
        /**
         * 判空
         */
        System.out.println(code);
        System.out.println(key);
        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码错误");
        }
        /**
         * 从redis缓存中获取存储的验证码与用户输入的验证码进行匹配
         */
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
        }
        /**
         * 如果正确就从redis中删除验证码信息，一次性地使用
         */
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
