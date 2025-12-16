package com.example.sys_newwelcome.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysLogs;
import com.example.sys_newwelcome.mapper.SysLogsMapper;
import com.example.sys_newwelcome.service.SysLogsService;
import com.example.sys_newwelcome.service.SysUserService;
import com.example.sys_newwelcome.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统日志相关接口
 */
@RestController
@RequestMapping("/sys/logs")
@Slf4j
public class SysLogsController {

    @Autowired
    SysLogsService sysLogsService;

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysLogsMapper sysLogsMapper;
    @Autowired
    PageUtils pageUtils;
    @Autowired
    HttpServletRequest req;


    /**
     * 删除信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean flagLog = sysLogsService.removeById(id);
        return Result.success(flagLog);
    }


    /**
     * 批量删除信息
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        boolean flagLogs = sysLogsService.removeByIds(ids);
        return Result.success(flagLogs);
    }

//    /**
//     * 多条件模糊查询信息
//     * pageNum 当前的页码
//     * pageSize 每页查询的个数
//     */
//    @GetMapping("/selectByPage")
//    public Result selectByPage(@RequestParam Integer pageNum,
//                               @RequestParam Integer pageSize,
//                               @RequestParam String operation,
//                               @RequestParam String type,
//                               @RequestParam String optUser) {
//        QueryWrapper<SysLogs> queryWrapper = new QueryWrapper<SysLogs>().orderByDesc("id");  // 默认倒序，让最新的数据在最上面
//        log.info("operation {}",operation);
//        queryWrapper.like(StrUtil.isNotBlank(type), "type", type);
//        Page<SysLogs> page = sysLogsService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        return Result.success(page);
//    }

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam(required = false) Integer pageNum,
                               @RequestParam(required = false) Integer pageSize,
                               @RequestParam(required = false) String operation,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String optUser) {
        QueryWrapper<SysLogs> queryWrapper = new QueryWrapper<SysLogs>().orderByDesc("id");  // 默认倒序，让最新的数据在最上面
        log.info("operation {}", operation);

        if (operation == null && optUser == null) {
            // 情况一：operation为null且optUser为null，直接查询type匹配的数据
            queryWrapper.like(StrUtil.isNotBlank(type), "type", type);
        } else if (operation != null && optUser == null) {
            // 情况二：operation不为null，optUser为null，查询operation和type都匹配的数据
            queryWrapper.eq(StrUtil.isNotBlank(operation), "operation", operation)
                    .like(StrUtil.isNotBlank(type), "type", type);
        } else if (operation == null && optUser != null) {
            // 情况三：operation为null，optUser不为null，查询optUser和type都匹配的数据
            queryWrapper.eq(StrUtil.isNotBlank(optUser), "user", optUser)
                    .like(StrUtil.isNotBlank(type), "type", type);
        } else if (operation != null && optUser != null) {
            // 情况四：两个都不为null，查询三个都匹配的数据
            queryWrapper.eq(StrUtil.isNotBlank(operation), "operation", operation)
                    .eq(StrUtil.isNotBlank(optUser), "user", optUser)
                    .like(StrUtil.isNotBlank(type), "type", type);
        }

        Page<SysLogs> page = sysLogsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

}