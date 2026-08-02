package com.example.sys_newwelcome.config;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.config.KaptchaConfig
 * @version:1.0
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        /**
         * 验证码边框样式无边框
         */
        properties.put("kaptcha.border", "no");
        /**
         * 验证码文本颜色为黑
         */
        properties.put("kaptcha.textproducer.font.color", "black");
        /**
         * 验证码文本字符间距离配置为‘4’
         */
        properties.put("kaptcha.textproducer.char.space", "4");
        /**
         * 验证码图片的高度配置为‘40’像素
         */
        properties.put("kaptcha.image.height", "40");
        /**
         * 验证码图片的宽度配置为‘120’像素
         */
        properties.put("kaptcha.image.width", "120");
        /**
         * 验证码文本的字体大小配置为‘30’像素
         */
        properties.put("kaptcha.textproducer.font.size", "30");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
