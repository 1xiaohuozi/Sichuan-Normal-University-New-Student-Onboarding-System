package com.example.sys_newwelcome.security;

import cn.hutool.json.JSONUtil;
import com.example.sys_newwelcome.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.LoginFailureHandler
 * @version:1.0
 */

/**
 * security身份验证失败处理器
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    /**
     * request获取请求的信息
     * response向客户端发送响应
     * exception表示身份验证失败的异常包含失败的详细信息
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        Result result = Result.fail(exception.getMessage().equals("Bad credentials") ? "用户名或者密码错误" : exception.getMessage());
        /**
         * 响应输出流，发送到客户端
         */
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        /**
         * 刷新输出流，确保所有数据都被写入到输出中
         */
        outputStream.flush();
        /**
         * 关闭输出流，释放资源完成响应的发送
         */
        outputStream.close();
    }
}

