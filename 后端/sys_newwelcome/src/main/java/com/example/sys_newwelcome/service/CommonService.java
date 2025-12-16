package com.example.sys_newwelcome.service;

import com.example.sys_newwelcome.common.email.param.LoginParam;
import com.example.sys_newwelcome.common.email.vo.R;

public interface CommonService {

    /**
     * 获取请求权限码
     * @param emailJson 邮箱
     * @return
     */
    R getRequestPermissionCode(String emailJson);

    /**
     * 发送邮箱验证码
     * @param loginParam （邮箱和权限码）
     * @return
     */
    R sendEmailCode(LoginParam loginParam);
}

