package com.example.sys_newwelcome.common.email.param;

import lombok.Getter;

@Getter
public class LoginParam {

    private String email; // 邮箱

    private String password1; // 密码

    private String passwordConfirm; // 确认密码

    private String code; // 验证码
}

