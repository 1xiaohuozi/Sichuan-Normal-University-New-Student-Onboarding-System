package com.example.sys_newwelcome.security;

import cn.hutool.core.util.StrUtil;
import com.example.sys_newwelcome.common.aspect.LogsAspect;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.service.SysUserService;
import com.example.sys_newwelcome.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.JwtAuthenticationFilter
 * @version:1.0
 */

/**
 * 扩展BasicAuthenticationFilter过滤器，JWT认证
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter{
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * AuthenticationManager负责处理用户身份验证
     * 检查用户名和密码是否正确，确定用户是否有权访问受保护的资源
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    /**
     * 执行过滤操作
     * chain用于继续请求处理的对象
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, SecurityException, ServletException {
        /**
         * 从请求头中获取JWT
         */
        String jwt = request.getHeader(jwtUtils.getHeader());
        /**
         * 从request这种获取ip
         */
        String clientIp = request.getRemoteAddr();
        /**
         * 检查JWT是否存在（空或未定义）
         * 如果是直接通过过滤器链继续请求处理
         */
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }
        /**
         * 解析JWT获取其中的声明信息（用户名，过期时间等）
         */
        Claims claims = jwtUtils.getClaimByToken(jwt);
        /**
         * 检查声明信息若为空，抛出token异常
         */
        if(ObjectUtils.isEmpty(claims)){
            throw new JwtException("token异常");
        }
        /**
         * 若token过去，抛出token已经过去
         */
        if(jwtUtils.isTokenExpired(claims)){
            throw new JwtException("token已经过期");
        }
        /**
         * 获取用户名
         */
        String username = claims.getSubject();
        /**
         * 根据用户名获取sysUser对象
         */
        SysUser sysUser = sysUserService.getByUserName(username);

        /**
         * 以用户名创建UsernamePasswordAuthenticationToken对象，不含密码等信息
         */
        LogsAspect.setIp(clientIp);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,null,userDetailService.getUserAuthority(sysUser.getId()));
        /**
         * 将令牌设置到security上下文，用户已经经过身份验证
         */
        SecurityContextHolder.getContext().setAuthentication(token);
        /**
         * 继续处理请求传给后续过滤器
         */
        chain.doFilter(request,response);
    }
}
