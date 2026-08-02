package com.example.sys_newwelcome.config;

import com.example.sys_newwelcome.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.config.SecurityConfig
 * @version:1.0
 */
@Configuration
/**
 * 启用security
 */
@EnableWebSecurity
/**
 * 运行使用@PreAuthorize和@PostAuthorize来控制方法的访问权限
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private CaptchaFilter captchaFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    /**
     * 由Spring容器管理，其他地方注入或引用它，以实现JWT身份验证
     */
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    /**
     * 用户密码进行安全的BCrypt哈希加密
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 定义一组URL路径，这些路径不需要身份验证即可访问
     */
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/sys/user/status",
            "/sys/user/perInfo",

            "/sys/notice/list",

            "/sys/report/list",
            "/sys/report/save",
            "/sys/report/status",


            "/sys/report/count/institute",
            "/sys/payment/save",
            "/sys/payment/list",
            "/sys/payment/status",
            "/sys/payment/nopass",
            "/sys/payment/info",
            "/sys/payment/exportExcel",

            "/sys/channel/nopass",
            "/sys/user/branch/institute",
            "/sys/user/branch/speciality",
            "/sys/user/repass",
            "/sys/payment/count",
            "/sys/channel/count",
            "/sys/channel/exportExcel",
            "/sys/channel/save",
            "/sys/channel/list",
            "/sys/payment/count/institute",
            "/sys/channel/count/institute",
            "/sys/report/list/institute",
            "/sys/payment/list/institute",
            "/sys/channel/list/institute",
            "/sys/dormitory/save",
            "/sys/dormitory/list",
            "/sys/dormitory/update",
            "/sys/dormitory/delete",
            "/sys/dormitory/info",
            "/sys/dormitory/delete",
            "/sys/dormitory/branch/institute",
            "/sys/notice/info",
            "/sys/report/exportExcel",
            "/sys/report/update",
            "/sys/user/findPassword",
            "/common/code/request",
            "/common/code/email",
            "/favicon.ico",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 启用CORS（跨域资源共享）
         * 禁用CSRF（跨站请求伪造）防护
         */
        http.cors().and().csrf().disable()
                /**
                 * 通过表单输入用户名和密码进行是身份验证
                 */
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                /**
                 * 注销退出
                 */
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                /**
                 * 禁用session
                 */
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                /**
                 * 配置拦截规则
                 */
                .and()
                .authorizeRequests()
                /**
                 * 表明白名单中的URL不需要身份验证，其他所有请求都需要身份验证
                 */
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
                /**
                 * 异常处理器
                 */
                .and()
                .exceptionHandling()
                /**
                 * 未经身份验证访问受保护资源时
                 */
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                /**
                 * 通过身份验证无权限访问保护资源时
                 */
                .accessDeniedHandler(jwtAccessDeniedHandler)
            /**
             * 配置自定义的过滤器
             */
            /**
                 * JWT身份验证过滤器
                 */
                .and()
                .addFilter(jwtAuthenticationFilter())
                /**
                 * 验证码过滤器,处理基于用户名和密码的身份验证
                 */
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }

    /**
     * 配置如何进行身份验证
     * （我定义了userDetailService实现了UserDetailsService接口）
     * 当用户尝试登录时，SpringSecurity会调用其中的方法根据用户的详细信息进行身份验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailService);
    }
}
