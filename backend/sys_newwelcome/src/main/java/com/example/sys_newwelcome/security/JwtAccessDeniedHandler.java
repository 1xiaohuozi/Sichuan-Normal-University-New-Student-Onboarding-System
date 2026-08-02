package com.example.sys_newwelcome.security;

import cn.hutool.json.JSONUtil;
import com.example.sys_newwelcome.common.lang.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.JwtAccessDeniedHandler
 * @version:1.0
 */
/**
 * 认证异常处理器（权限不足被拒绝）
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 当用户通过身份认证尝试访问某个资源但由于权限不足被拒绝时被调用
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{
        /**
         * 设置HTTP响应的内容类型为JSON，字符编码为UTF-8
         */
        response.setContentType("application/json;charset=UTF-8");
        /**
         * 设置HTTP响应的状态码为403，表示访问拒绝
         */
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        /**
         * 获取HTTP响应的输出流，发送有关访问被拒绝的错误信息
         */
        ServletOutputStream outputStream = response.getOutputStream();
        /**
         * 创建result对象包含有关访问被拒绝的错误消息
         */
        Result result = Result.fail(accessDeniedException.getMessage());
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
