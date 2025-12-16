package com.example.sys_newwelcome.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.dto.SysMenuDto;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysMenu;
import com.example.sys_newwelcome.entity.SysRoleMenu;
import com.example.sys_newwelcome.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  菜单前端控制器
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.SysMenuController
 * @version:1.0
 */
@RestController
@Api(value = "菜单管理", tags = "菜单接口")
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    /**
     * principal用于获取当前登录用户的主体信息
     */
    @ApiOperation(value = "获取当前用户导航菜单", notes = "获取当前登录用户的导航菜单")
    @GetMapping("/nav")
    public Result nav(Principal principal){
        /**
         * 获取当前登录用户的信息
         */
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        /**
         * 获取当前用户权限信息
         */
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        /**
         * 将权限信息字符串拆分为一个字符串数组，每一个元素表示一个权限
         */
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo,"");
        /**
         * 获取当前用户可以访问的导航菜单信息
         */
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();
        /**
         * 构建权限信息和导航菜单的map并作为Result返回
         */
        return Result.success(
                MapUtil.builder()
                        .put("authoritys",authorityInfoArray)
                        .put("nav",navs)
                        .map()
        );

    }

    @ApiOperation(value = "获取菜单信息", notes = "根据ID获取菜单详细信息")
    @GetMapping("/info/{id}")
    /**
     * 必须拥有sys:menu:list权限才能访问这个方法
     */
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    public Result info(@PathVariable(name = "id") Long id) {
        /**
         * 返回菜单详细信息
         */
        return Result.success(sysMenuService.getById(id));
    }

    @ApiOperation(value = "获取菜单列表", notes = "获取树形菜单列表")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    public Result list(){
        /**
         * 返回树形菜单列表
         */
        List<SysMenu> menus = sysMenuService.tree();
        return Result.success(menus);
    }

    /**
     * 保存创建新的菜单项
     */
    @ApiOperation(value = "保存菜单", notes = "创建新的菜单项")
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('sys:menu:save')")
    @HoneyLogs(operation = "菜单", type = "新增",url = "sys:menu:save")
    public Result save(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setCreated(LocalDateTime.now());
        sysMenuService.save(sysMenu);
        return Result.success(sysMenu);
    }

    /**
     * 更新菜单
     */
    @ApiOperation(value = "更新菜单", notes = "更新菜单信息")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('sys:menu:update')")
    @HoneyLogs(operation = "菜单", type = "修改",url = "sys:menu:update")
    public Result update(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setUpdated(LocalDateTime.now());
        /**
         * 调用id更新菜单的信息
         */
        sysMenuService.updateById(sysMenu);
        /**
         * 清除与该菜单相关的用户权限信息
         */
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        return Result.success(sysMenu);
    }

    @ApiOperation(value = "删除菜单", notes = "根据ID删除菜单项")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('sys:menu:delete')")
    @HoneyLogs(operation = "菜单", type = "删除",url = "sys:menu:delete")
    public Result delete(@PathVariable("id") Long id){
        /**
         * 先查询有无子菜单
         */
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id",id));
        if(count > 0){
            return Result.fail("请先删除子菜单");
        }
        /**
         * 若没有子菜单则清除与该菜单项相关的用户权限信息
         */
        sysUserService.clearUserAuthorityInfoByMenuId(id);
        /**
         * 删除id的菜单项
         */
        sysMenuService.removeById(id);
        /**
         * 删除与该菜单相关的角色菜单关联
         */
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",id));
        return Result.success("");
    }
}
