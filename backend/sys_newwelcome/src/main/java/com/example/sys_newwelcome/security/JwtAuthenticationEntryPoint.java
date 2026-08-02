package com.example.sys_newwelcome.security;

import cn.hutool.json.JSONUtil;
import com.example.sys_newwelcome.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.JwtAuthenticationEntryPoint
 * @version:1.0
 */
/**
 * 认证失败处理器（处理认证失败是的逻辑）
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 当用户未通过身份验证尝试访问受保护的资源时被调用
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
        /**
         * 设置HTTP响应的内容类型为JSON，字符编码为UTF-8
         */
        response.setContentType("application/json;charset=UTF-8");
        /**
         * 设置HTTP响应的状态码为401，表示未经身份验证
         */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        /**
         * 获取HTTP响应的输出流，发送有关访问被拒绝的错误信息
         */
        ServletOutputStream outputStream = response.getOutputStream();
        /**
         * 创建result用户，包含错误信息
         */
        Result result = Result.fail("请先登录");
        /**
         * 将result对象转换为JSON字符串，并将其写入响应输出流中，向客户端发送错误响应
         * 字符编码UTF-8
         */
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        /**
         * 刷新、关闭输出流
         */
        outputStream.flush();
        outputStream.close();
    }

}
