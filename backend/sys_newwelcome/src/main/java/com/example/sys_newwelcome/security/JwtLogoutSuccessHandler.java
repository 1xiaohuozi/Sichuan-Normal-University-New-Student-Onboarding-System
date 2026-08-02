package com.example.sys_newwelcome.security;

import cn.hutool.json.JSONUtil;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.JwtLogoutSuccessHandler
 * @version:1.0
 */

/**
 * 处理用户注销（等出）
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        /**
         * 用户已经认证
         */
        if(authentication != null){
            /**
             * 注销。清除用户的安全上下文，确保用户不再具有有效的身份认证信息
             */
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        /**
         * 响应的内容类型为JSON
         */
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        /**
         * 清除JWT头部信息，表示用户已经注销
         */
        response.setHeader(jwtUtils.getHeader(),null);
        Result result = Result.success("成功");
        /**
         * 转换Result对象为JSON字符串，写入响应输出流
         */
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        /**
         * 刷新并退出关闭输出流
         */
        outputStream.flush();
        outputStream.close();
    }
}
