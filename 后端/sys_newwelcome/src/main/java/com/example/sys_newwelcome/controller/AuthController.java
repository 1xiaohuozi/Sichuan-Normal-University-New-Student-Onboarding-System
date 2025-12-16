package com.example.sys_newwelcome.controller;

import cn.hutool.core.map.MapUtil;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.lang.Const;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysUser;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;
/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.AuthController
 * @version:1.0
 */

/**
 * 生成验证码图片并将验证码文本和相关信息存储在Redis中
 * 前端可以获取验证码图像并验证用户输入的验证码
 */
@Api(value = "验证码管理", tags = "验证码接口")
@RestController
public class AuthController extends BaseController {
    /**
     * 生成验证码
     */
    @Autowired
    Producer producer;

    @ApiOperation(value = "获取验证码图片", notes = "生成验证码图片并将验证码文本存储在Redis中")
    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        /**
         * 生成随机的key，是一个UUID字符串
         */
        String key = UUID.randomUUID().toString();
        /**
         * 生成验证码文本（字符串）
         */
        String code = producer.createText();
        /**
         * 生成响应的验证码图像
         */
        BufferedImage image = producer.createImage(code);
        /**
         * 创建字节数组输出流
         */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        /**
         * 放入验证码图片
         */
        ImageIO.write(image, "jpg", outputStream);
        /**
         * 编码成Base64格式字符串
         */
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        /**
         * 将最终连接城的字符串存入Redis中
         * key之前随机生成的，关联验证码文本和图像
         * code生成的验证码文本
         * 验证码在缓存中的有效期为120s，120s后将自动从redis中删除
         */
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        /**
         * 验证码生成成功响应
         */
        return Result.success(
                /**
                 * Map存储响应的键值对
                 */
                MapUtil.builder()
                        /**
                         * key唯一标识
                         */
                        .put("token", key)
                        /**
                         * 前端通过获取base64Img显示验证码图片
                         */
                        .put("captchaImg", base64Img)
                        /**
                         * Map构建操作完成封装为一个不可变的Map对象
                         */
                        .build()
        );
    }


    @GetMapping("/sys/userInfo")
    /**
     * 获取已认证的用户信息
     */
    @ApiOperation(value = "获取已认证的用户信息", notes = "获取已认证的用户的信息")
    public Result userInfo(Principal principal) {
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        return Result.success(MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("name", sysUser.getName())
                .put("avatar", sysUser.getAvatar())
                .put("created", sysUser.getCreated())
                .map()
        );
    }

    @GetMapping("/sys/userDetailInfo")
    /**
     * 获取已认证的用户详细信息
     */
    @HoneyLogs(operation = "用户", type = "登录",url = "login")
    @ApiOperation(value = "获取已认证的用户个人详细信息", notes = "获取已认证的用户的个人详细信息")
    public Result detailInfo(Principal principal) {
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        return Result.success(sysUser);
    }
//    /**
//     * 获取用户个人的详细
//     */
//    @ApiOperation(value = "获取用户个人信息", notes = "根据ID获取用户个人的详细信息")
//    @GetMapping("/perInfo")
//    public Result perInfo(@RequestBody Long id){
//        /**
//         * 根据id查询用户信息
//         */
//        SysUser sysUser = sysUserService.getById(id);
//        /**
//         * 获取用户相关联角色列表
//         */
//        List<SysRole> roles = sysRoleService.listRolesByUserId(id);
//        /**
//         * 设置角色
//         */
//        sysUser.setSysRoles(roles);
//        return Result.success(sysUser);
//    }
}
