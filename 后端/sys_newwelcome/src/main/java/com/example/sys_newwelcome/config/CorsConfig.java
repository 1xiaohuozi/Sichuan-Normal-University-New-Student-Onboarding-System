package com.example.sys_newwelcome.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.config.CorsConfig
 * @version:1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     *  创建CorsConfiguration，配置其属性
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /**
         * 允许任何来源的请求
         */
        corsConfiguration.addAllowedOrigin("*");
        /**
         * 允许携带任何请求头
         */
        corsConfiguration.addAllowedHeader("*");
        /**
         * 允许使用任何HTTP请求方法
         */
        corsConfiguration.addAllowedMethod("*");
        /**
         * 允许浏览器访问‘Authorization’响应头
         */
        corsConfiguration.addExposedHeader("Authorization");
        return corsConfiguration;
    }

    /**
     *创建CorsFilter过滤器，处理跨域请求
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        /**
         * 用于所有路径
         */
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                /**
                 * 允许任何来源
                 */
                .allowedOrigins("*")
//          .allowCredentials(true)
                /**
                 * 允许指定的http请求
                 */
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                /**
                 * 设置最大缓存h时间为3600秒
                 */
                .maxAge(3600);
    }
}

