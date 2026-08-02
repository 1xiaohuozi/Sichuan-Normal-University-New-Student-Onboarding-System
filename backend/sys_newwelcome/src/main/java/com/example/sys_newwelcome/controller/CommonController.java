package com.example.sys_newwelcome.controller;

import com.example.sys_newwelcome.common.email.param.LoginParam;
import com.example.sys_newwelcome.common.email.vo.R;
import com.example.sys_newwelcome.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
@CrossOrigin
public class CommonController {

    @Autowired
    private CommonService commonService;

    // 权限码请求接口
    @PostMapping("/code/request")
    public R getRequestPermissionCode(@RequestBody String emailJson) {
        return commonService.getRequestPermissionCode(emailJson);
    }

    // 邮箱验证码接口
    @PostMapping("/code/email")
    public R sendEmailCode(@RequestBody LoginParam loginParam) {
        return commonService.sendEmailCode(loginParam);
    }
}
