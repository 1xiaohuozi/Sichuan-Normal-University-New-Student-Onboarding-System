package com.example.sys_newwelcome.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysRole;
import com.example.sys_newwelcome.entity.SysRoleMenu;
import com.example.sys_newwelcome.entity.SysUserRole;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  角色前端控制器
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.SysRoleController
 * @version:1.0
 */
@RestController
@Api(value = "角色管理", tags = "角色接口")
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    /**
     * 获取角色的详细信息
     */
    @ApiOperation(value = "获取角色信息", notes = "根据ID获取角色的详细信息")
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
        /**
         * 获取具有给定id的角色的详细信息
         */
        SysRole sysRole = sysRoleService.getById(id);
        /**
         * 获取与该角色相关的菜单列表
         */
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id",id));
        /**
         * 通过六操作从角色与菜单关联的列表中提取菜单项id
         * 并且将其收集到一个新的列表中
         */
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        /**
         * 将菜单的id列表设置到与角色对应的对象中
         */
        sysRole.setMenuIds(menuIds);
        return Result.success(sysRole);
    }

    /**
     * 获取角色列表
     */
    @ApiOperation(value = "获取角色列表", notes = "根据角色名称")
//    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    @GetMapping("/list")
    public Result list(String name){
        /**
         * 分页查询角色数据
         */
        Page<SysRole> pageData = sysRoleService.page(getPage(),new QueryWrapper<SysRole>().like(StrUtil.isNotBlank(name),"name",name));
        return Result.success(pageData);
    }

    /**
     * 创建保存新的角色是
     */
    @ApiOperation(value = "创建角色", notes = "创建新的角色项")
    @PreAuthorize("hasAnyAuthority('sys:role:save')")
    @PostMapping("/save")
    @HoneyLogs(operation = "角色", type = "新增",url = "sys:role:save")
    public Result save(@Validated @RequestBody SysRole sysRole){
        sysRole.setCreated(LocalDateTime.now());
        /**
         * 设置角色的状态为开启
         */
//        sysRole.setStatu(Const.STATUS_ON);
        sysRoleService.save(sysRole);
        return Result.success(sysRole);
    }

    /**
     * 更新角色
     */
    @ApiOperation(value = "更新角色信息", notes = "更新角色的详细信息")
    @PreAuthorize("hasAnyAuthority('sys:role:update')")
    @PostMapping("/update")
    @HoneyLogs(operation = "角色", type = "修改",url = "sys:role:update")
    public Result update(@Validated @RequestBody SysRole sysRole){
        SysRole sysRole1 = sysRoleService.getById(sysRole.getId());
        sysRole.setUpdated(LocalDateTime.now());
        sysRoleService.updateById(sysRole);
        /**
         * 清理与该角色相关的角色权限信息
         * 更新缓存
         */
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        List<String> stringList = new ArrayList<>();
        stringList.add(sysRole1.toString());
        stringList.add(sysRole.toString());
        return Result.success(stringList);
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色", notes = "根据角色ID删除角色项")
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    @PostMapping("/delete")
    /**
     * 声明事务，保证数据库操作是完整一致的
     */
    @Transactional
    @HoneyLogs(operation = "角色", type = "删除",url = "sys:role:delete")
    public Result info(@RequestBody Long[] ids){
        /**
         * 删除ids数组中id对应的角色
         */
        boolean flagRole = sysRoleService.removeByIds(Arrays.asList(ids));
        /**
         * 删除中间表UserRole和RoleMenu表中的关联角色的信息
         */
        boolean flagUserRole = sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",ids));
        boolean flagRoleMenu = sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id",ids));
        /**
         * 循环清除缓存中的关于角色的用户权限信息
         */
        Arrays.stream(ids).forEach(id -> {
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });
        return Result.success(flagRole && flagUserRole && flagRoleMenu);
    }

    /**
     * 更新角色权限
     */
    @ApiOperation(value = "更新角色权限", notes = "更新角色的权限信息")
    @PreAuthorize("hasAnyAuthority('sys:role:perm')")
    @PostMapping("/perm/{roleId}")
    @Transactional
    @HoneyLogs(operation = "权限", type = "修改",url = "sys:role:perm")
    public Result info(@PathVariable("roleId") Long roleId,@RequestBody Long[] menuIds){
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        /**
         * 每个菜单Id都创建一个SysRoleMenu对象添加到sysRoleMenus列表中
         */
        Arrays.stream(menuIds).forEach(menuId ->{
            SysRoleMenu roleMenu = new SysRoleMenu();
            if(roleMenu != null){
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(roleId);
                sysRoleMenus.add(roleMenu);
            }
        });

        /**
         * 删除旧的与指定角色id相关的所有角色菜单关联信息
         */
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        /**
         * 将新的角色菜单关联信息批量保存到数据库（角色分配新的权限的信息）
         */
        sysRoleMenuService.saveBatch(sysRoleMenus);
        /**
         * 删除缓存
         */
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);
        return Result.success(menuIds);
    }
}
