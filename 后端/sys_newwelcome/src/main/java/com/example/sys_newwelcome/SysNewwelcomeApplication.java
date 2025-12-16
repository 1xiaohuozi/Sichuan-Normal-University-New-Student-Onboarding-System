package com.example.sys_newwelcome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.SysNewwelcomeApplication
 * @version:1.0
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("com.example.sys_newwelcome.mapper")
@ComponentScan(basePackages = "com.example")
public class SysNewwelcomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysNewwelcomeApplication.class, args);
    }

}
