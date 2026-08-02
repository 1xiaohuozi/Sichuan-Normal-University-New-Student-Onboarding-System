package com.example.sys_newwelcome.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.entity.SysDormitory;
import com.example.sys_newwelcome.service.*;
import com.example.sys_newwelcome.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.BaseController
 * @version:1.0
 */
public class BaseController {
    /**
     * 允许在控制器方法中访问HTTP请求的相关信息
     */
    @Autowired
    HttpServletRequest req;
    /**
     * 在应用程序中与Redis缓存进行交互
     */
    @Autowired
    RedisUtils redisUtil;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysNoticeService sysNoticeService;
    @Autowired
    SysReportService sysReportService;
    @Autowired
    SysPaymentService sysPaymentService;
    @Autowired
    SysChannelService sysChannelService;
    @Autowired
    SysDormitoryService sysDormitoryService;

    /**
     * 创建分页对象
     */
    public Page getPage(){
        /**
         * 获取名为current的参数的整数值，如果参数不存在则默认使用1为当前页数
         */
        int current = ServletRequestUtils.getIntParameter(req,"current",1);
        /**
         * 获取名为size的参数的整数值，如果参数不存在则默认使用10作为每页的数据条数
         */
        int size = ServletRequestUtils.getIntParameter(req,"size",40);
        /**
         * 返回分页对象，当前页数和每页数据条数的对象
         */
        return new Page(current,size);
    }
}

