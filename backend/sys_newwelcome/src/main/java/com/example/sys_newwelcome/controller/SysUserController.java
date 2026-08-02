package com.example.sys_newwelcome.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.dto.PassDto;
import com.example.sys_newwelcome.common.dto.SysInstituteClassNumber;
import com.example.sys_newwelcome.common.dto.SysUserDto;
import com.example.sys_newwelcome.common.email.param.LoginParam;
import com.example.sys_newwelcome.common.email.vo.R;
import com.example.sys_newwelcome.common.lang.Const;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.*;
import com.example.sys_newwelcome.mapper.SysRoleMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysPaymentService;
import com.example.sys_newwelcome.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 *  用户管理前端控制器
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.SysUserController
 * @version:1.0
 */
@RestController
@Api(value = "用户管理", tags = "用户接口")
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {


    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    PageUtils pageUtil;
    /**
     * 获取特定用户的详细
     */
    @ApiOperation(value = "获取用户信息", notes = "根据ID获取用户的详细信息")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
        /**
         * 根据id查询用户信息
         */
        SysUser sysUser = sysUserService.getById(id);
        /**
         * 使用断言赖检查获取用户对象是否为空
         */
        Assert.notNull(sysUser,"找不到该管理员");
        /**
         * 获取用户相关联角色列表
         */
        List<SysRole> roles = sysRoleService.listRolesByUserId(id);
        /**
         * 设置角色
         */
        sysUser.setSysRoles(roles);
        return Result.success(sysUser);
    }

    /**
     * 确认信息状态
     */
    @ApiOperation(value = "更新用户信息状态", notes = "更新信息状态")
//    @PreAuthorize("hasAnyAuthority('sys:user:status')")
    @PostMapping("/status")
    public Result status(@RequestBody Long id){

        /**
         * 查询id对应SysUser信息
         */
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);

        /**
         * 存状态
         */
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        sysUser.setState(1);
        sysUserService.update(sysUser,queryWrapper);
        return Result.success(sysUser);
    }


    /**
     * 获取用户列表
     */
    @ApiOperation(value = "获取用户列表", notes = "根据用户名获取用户列表")
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list(String username){
        /**
         * 分页查询用户数据
         */
        Page<SysUser> pageData = sysUserService.page(
                getPage(),
                new QueryWrapper<SysUser>()
                        .eq(StrUtil.isNotBlank(username), "username", username)
                        .orderByDesc("institute", "speciality", "class_number")
        );
        /**
         * 获取与用户相关联的角色列表
         */
        pageData.getRecords().forEach(u ->{
            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
        });
        return Result.success(pageData);
    }

//    /**
//     * 根据学院获取用户列表
//     */
//    @ApiOperation(value = "根据学院获取用户列表", notes = "根据学院获取用户列表")
////    @PreAuthorize("hasAnyAuthority('sys:user:listOfCollege')")
//    @GetMapping("/listOfCollege")
//    public Result listOfCollege(@RequestBody Long userId){
//
//        /**
//         * 分页查询用户数据
//         */
//        Page<SysUser> pageData = sysUserService.page(getPage(),new QueryWrapper<SysUser>().like(StrUtil.isNotBlank(username),"username",username));
//        /**
//         * 获取与用户相关联的角色列表
//         */
//        pageData.getRecords().forEach(u ->{
//            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
//        });
//        return Result.success(pageData);
//    }

    /**
     * 创建保存新的用户
     */
    @ApiOperation(value = "创建用户", notes = "创建新的用户")
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    @PostMapping("/save")
    @HoneyLogs(operation = "用户", type = "新增",url = "sys:user:save")
    public Result save(@Validated @RequestBody SysUser sysUser){
        /**
         * 设置创建时间和开启状态
         */
        sysUser.setCreated(LocalDateTime.now());
//        sysUser.setStatu(Const.STATUS_ON);

        /**
         * 对密码进行加密， 默认密码为888888
         */
        String password = passwordEncoder.encode(Const.DEFAULT_PASSWORD);
        sysUser.setPassword(password);
        /**
         * 默认头像
         */
        sysUser.setAvatar(Const.DEFAULT_AVATAR);
        sysUserService.save(sysUser);
        return Result.success(sysUser);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "更新用户的详细信息")
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    @PostMapping("/update")
    @HoneyLogs(operation = "用户", type = "修改",url = "sys:user:update")
    public Result update(@Validated @RequestBody SysUser sysUser){
        SysUser sysUser1 = sysUserService.getById(sysUser.getId());
        sysUserService.updateById(sysUser);
        List<String> stringList = new ArrayList<>();
        stringList.add(sysUser1.toString());
        stringList.add(sysUser.toString());
        return Result.success(stringList);
    }

    /**
     * 删除用户
     */
    @ApiOperation(value = "删除用户", notes = "根据用户ID删除用户")
//    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    @PostMapping("/delete")
    @Transactional
    @HoneyLogs(operation = "用户", type = "删除",url = "sys:user:delete")
    public Result delete(@RequestBody Long[] ids){

        SysUser[] sysUsers = new SysUser[ids.length];
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            SysUser sysUser = sysUserService.getById(id);
            sysUsers[i] = sysUser;
        }
        /**
         * 删除用户信息
         */
        boolean flagSysUser = sysUserService.removeByIds(Arrays.asList(ids));

        /**
         * 删除中间表
         */
        boolean flagSysUserRole = sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));

        /**
         * 删除对应报道信息
         */
        boolean flagSysUserReport = sysReportService.remove(new QueryWrapper<SysReport>().in("user_id",ids));
        /**
         * 删除对应缴费信息
         */
        boolean flagSysUserPayment = sysPaymentService.remove(new QueryWrapper<SysPayment>().in("user_id",ids));
        /**
         * 删除对应绿色通道信息
         */
        boolean flagSysUserChannel = sysChannelService.remove(new QueryWrapper<SysChannel>().in("user_id",ids));
        /**
         * 删除对应宿舍选取信息
         */
        boolean flagSysUserDormitory = sysDormitoryService.remove(new QueryWrapper<SysDormitory>().in("user_id",ids));
        return Result.success(Arrays.toString(sysUsers));
    }


    /**
     * 给用户分配角色功能
     */
//    @HoneyLogs(operation = "用户", type = "角色分配")
    @ApiOperation(value = "分配角色", notes = "给用户分配角色")
    @PreAuthorize("hasAnyAuthority('sys:user:role')")
    @PostMapping("/role/{userId}")
    @Transactional
    public Result rolePerm(@PathVariable("userId") Long userId,@RequestBody Long[] roleIds){
        /**
         * 获取没有分配角色的用户信息
         */
        SysUser sysUser1 = sysUserService.getById(userId);
        /**
         * 获取角色id对应的角色信息
         */
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        SysRole[] sysRoles = new SysRole[roleIds.length];
        for (int i = 0; i < roleIds.length; i++) {
            Long id = roleIds[i];
            queryWrapper.eq("id",id);
            sysRoles[i] = sysRoleMapper.selectOne(queryWrapper);
        }
        SysUser sysUser2 = sysUser1;
//        sysUser2.setSysRole(sysRoles);
        List<SysUserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleIds).forEach(r ->{
            /**
             * 建立用户与角色的关联
             */
           SysUserRole sysUserRole = new SysUserRole();
           if(sysUserRole != null){
               sysUserRole.setRoleId(r);
               sysUserRole.setUserId(userId);
           }
           userRoles.add(sysUserRole);
        });
        /**
         * 删除用户相关的角色记录
         */
        boolean flagSysUserRole = sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        /**
         * 保存刚刚创建的用户角色记录批量保存到数据库中
         */
        sysUserRoleService.saveBatch(userRoles);
        /**
         * 删除缓存
         */
        SysUser sysUser = sysUserService.getById(userId);
        sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

        Object[] sysUserDetail = {sysUser1,sysUser2};
        return Result.success(Arrays.toString(sysUserDetail));
    }

    /**
     * 重置用户密码
     */
    @ApiOperation(value = "重置密码", notes = "重置用户密码为默认密码")
    @PreAuthorize("hasAnyAuthority('sys:user:repass')")
    @PostMapping("/repass")
    public Result repass(@RequestBody Long userId){
        /**
         * 通过Id获取用户信息
         */
        SysUser sysUser = sysUserService.getById(userId);
        /**
         * 设置用户密码重置为默认
         */
        sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
        sysUser.setUpdated(LocalDateTime.now());
        /**
         * 更新用户信息
         */
        boolean flagSysUser = sysUserService.updateById(sysUser);
        return Result.success(flagSysUser);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "修改用户密码")
    @PostMapping("/updatePass")
    @HoneyLogs(operation = "密码", type = "修改",url = "sys:user:updatePass")
    public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal){
        /**
         * 根据用户名获取用户信息
         */
        SysUser sysUser = sysUserService.getByUserName(principal.getName());
        /**
         * 匹配当前密码与输入的密码
         */
        boolean matches = passwordEncoder.matches(passDto.getCurrentPass(),sysUser.getPassword());
        if(!matches){
            return Result.fail("旧密码不正确");
        }
        /**
         * 设置新密码
         */
        String password = passwordEncoder.encode(passDto.getPassword());
        sysUser.setPassword(password);
        /**
         * 设置默认头像
         */
        sysUser.setAvatar(Const.DEFAULT_AVATAR);
        sysUserService.updateById(sysUser);
        return Result.success(sysUser);
    }

    /**
     * 学院筛选
     */
    @ApiOperation(value = "筛选学院信息", notes = "学院学生筛选")
//    @PreAuthorize("hasAnyAuthority('sys:user:branch:institute')")
    @PostMapping("/branch/institute")
    public Result branchInstitute(@RequestBody String institute){
        /**
         * 分页查询相应学院学生数据
         */
        List<SysUser> userList = sysUserService.list(new QueryWrapper<SysUser>().eq(StrUtil.isNotBlank(institute), "institute", institute));
        Page<SysUser> pageData = pageUtil.pageList(userList,req);
        /**
         * 获取与用户相关联的角色列表
         */
        pageData.getRecords().forEach(u ->{
            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
        });
        return Result.success(pageData);
    }

    /**
     * 学院筛选2
     */
    @ApiOperation(value = "筛选学院信息2", notes = "学院学生筛选")
//    @PreAuthorize("hasAnyAuthority('sys:user:branch:institute:classNumber')")
    @PostMapping("/branch/institute/classNumber")
    public Result branchInstitute2(@RequestBody SysInstituteClassNumber instituteClassNumber){
        /**
         * 分页查询相应学院学生数据
         */
        if(instituteClassNumber.getClassNumber().equals("all")){
            List<SysUser> userList = sysUserService.list(new QueryWrapper<SysUser>().eq(StrUtil.isNotBlank(instituteClassNumber.getInstitute()), "institute", instituteClassNumber.getInstitute()));
            Page<SysUser> pageData = pageUtil.pageList(userList,req);
            /**
             * 获取与用户相关联的角色列表
             */
            pageData.getRecords().forEach(u ->{
                u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
            });
            return Result.success(pageData);
        }else{
            List<SysUser> userList = sysUserService.list(new QueryWrapper<SysUser>()
                    .eq(StrUtil.isNotBlank(instituteClassNumber.getInstitute()), "institute", instituteClassNumber.getInstitute())
                    .eq(StrUtil.isNotBlank(instituteClassNumber.getClassNumber()), "class_number", instituteClassNumber.getClassNumber())
            );
            Page<SysUser> pageData = pageUtil.pageList(userList,req);
            /**
             * 获取与用户相关联的角色列表
             */
            pageData.getRecords().forEach(u ->{
                u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
            });
            return Result.success(pageData);
        }
    }

    // 找回密码
    @PostMapping("/findPassword")
    public R findPassword(@RequestBody LoginParam loginParam) {
        System.out.println(loginParam.getPassword1());
        System.out.println(loginParam.getCode());
        System.out.println(loginParam.getEmail());

        return sysUserService.findPassword(loginParam);
    }


//    /**
//     * 专业筛选
//     */
//    @ApiOperation(value = "筛选专业信息", notes = "专业学生筛选")
////    @PreAuthorize("hasAnyAuthority('sys:user:branch:speciality')")
//    @PostMapping("/branch/speciality")
//    public Result branchSpeciality(@RequestBody String institute,@RequestBody String speciality){
//        /**
//         * 分页查询相应专业学生数据
//         */
//        Page<SysUser> pageData = sysUserService.page(
//                getPage(),
//                new QueryWrapper<SysUser>()
//                        .eq(StrUtil.isNotBlank(institute), "institute", institute)
//                        .eq(StrUtil.isNotBlank(speciality), "speciality", speciality)
//        );
//
//        /**
//         * 获取与用户相关联的角色列表
//         */
//        pageData.getRecords().forEach(u ->{
//            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
//        });
//        return Result.success(pageData);
//    }
//
//    /**
//     * 班级筛选
//     */
//    @ApiOperation(value = "筛选班级信息", notes = "班级学生筛选")
////    @PreAuthorize("hasAnyAuthority('sys:user:branch:classNumber')")
//    @PostMapping("/branch/classNumber")
//    public Result branchClassNumber(@RequestBody String institute,@RequestBody String speciality,@RequestBody String classNumber){
//        /**
//         * 分页查询相应专业学生数据
//         */
//        Page<SysUser> pageData = sysUserService.page(
//                getPage(),
//                new QueryWrapper<SysUser>()
//                        .eq(StrUtil.isNotBlank(institute), "institute", institute)
//                        .eq(StrUtil.isNotBlank(speciality), "speciality", speciality)
//                        .eq(StrUtil.isNotBlank(classNumber), "class_number", classNumber)
//        );
//
//        /**
//         * 获取与用户相关联的角色列表
//         */
//        pageData.getRecords().forEach(u ->{
//            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
//        });
//        return Result.success(pageData);
//    }



}

