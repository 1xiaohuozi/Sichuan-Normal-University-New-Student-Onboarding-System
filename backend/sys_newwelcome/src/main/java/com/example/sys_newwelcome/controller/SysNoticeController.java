package com.example.sys_newwelcome.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.*;
import com.example.sys_newwelcome.mapper.SysNoticeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * <p>
 *  公告管理前端控制器
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.controller.SysNoticeController
 * @version:1.0
 */

@RestController
@Api(value = "公告管理", tags = "公告接口")
@RequestMapping("/sys/notice")
public class SysNoticeController extends BaseController{

    @Autowired
    SysNoticeMapper sysNoticeMapper;

    public SysNoticeController(SysNoticeMapper sysNoticeMapper) {
        this.sysNoticeMapper = sysNoticeMapper;
    }

    /**
     * 获取公告列表
     */
    @ApiOperation(value = "获取公告列表", notes = "根据公告名获取公告列表")
    @GetMapping("/list")
    public Result getNoticeList(String title){
        Page<SysNotice> pageData = sysNoticeService.page(getPage(),new QueryWrapper<SysNotice>().like(StrUtil.isNotBlank(title),"title",title));
        return Result.success(pageData);
    }

    /**
     * 获取公告信息
     */
    @ApiOperation(value = "获取公告信息", notes = "根据公告id获取公告信息")
    @PostMapping("/info")
    public Result info(@RequestBody Long id){
        SysNotice sysNotice = sysNoticeMapper.selectOne(new QueryWrapper<SysNotice>().eq("id",id));
        return Result.success(sysNotice);
    }

    /**
     * 创建保存新的公告
     */
    @ApiOperation(value = "创建公告", notes = "创建新的公告")
    @PreAuthorize("hasAnyAuthority('sys:notice:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysNotice sysNotice){
        /**
         * 设置创建时间和开启状态
         */
        sysNotice.setCreated(LocalDateTime.now());
        /**
         * sysNotice.setStatu(Const.STATUS_ON);
         */

        /**
         * 存公告信息
         */
        sysNoticeService.save(sysNotice);
        return Result.success(sysNotice);
    }


    /**
     * 修改公告信息
     */
    @ApiOperation(value = "更新公告", notes = "更新公告的详细信息")
    @PreAuthorize("hasAnyAuthority('sys:notice:update')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody SysNotice sysNotice){
        sysNotice.setUpdated(LocalDateTime.now());
        sysNoticeService.updateById(sysNotice);
        return Result.success(sysNotice);
    }

    /**
     * 删除公告
     */
    @ApiOperation(value = "删除公告", notes = "根据公告ID删除公告")
    @PreAuthorize("hasAnyAuthority('sys:notice:delete')")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        /**
         * 删除公告信息
         *
         */
        boolean flagSysUser = sysNoticeService.removeByIds(Arrays.asList(ids));
        return Result.success(flagSysUser);
    }

}
