package com.example.sys_newwelcome;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SysNewwelcomeApplicationTests {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        String password = bCryptPasswordEncoder.encode("1234");
        boolean matches = bCryptPasswordEncoder.matches("1234",password);
        System.out.println("匹配结果" + matches);
        System.out.println(password);
    }

}
