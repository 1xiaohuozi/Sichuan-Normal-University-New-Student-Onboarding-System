package com.example.sys_newwelcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.dto.SysInstituteClassNumber;
import com.example.sys_newwelcome.common.dto.SysReportDto;
import com.example.sys_newwelcome.common.dto.SysReportUserDto;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysDormitory;
import com.example.sys_newwelcome.entity.SysReport;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysReportMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(value = "报到信息管理", tags = "报到信息接口")
@RequestMapping("/sys/report")
@Slf4j
public class SysReportController extends BaseController{

    @Autowired
    SysReportMapper sysReportMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtils pageUtil;
    @Autowired
    HttpServletRequest req;

    /**
     * 获取报到信息列表
     */
    @ApiOperation(value = "获取报到信息列表", notes = "根据报道信息id获取报到列表")
    @GetMapping("/list")
    public Result getReportList() {
        List<SysReport> sysReportList = sysReportMapper.selectList(null);
        System.out.println(sysReportList);
        List<SysReportDto> sysReportDtoList = sysReportList.stream()
                .map(sysReport -> {
                    SysReportDto sysReportDto = new SysReportDto();
                    /**
                     * 将 SysReport 中的属性复制到 SysReportDto
                     */
                    BeanUtils.copyProperties(sysReport, sysReportDto);
                    QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("id", sysReport.getUserId());
                    SysUser sysUser = sysUserMapper.selectOne(queryWrapper1);
                    if (sysReportDto != null) {
                        sysReportDto.setName(sysUser.getName());
                        sysReportDto.setUsername(sysUser.getUsername());
                    }
                    return sysReportDto;
                })
                .collect(Collectors.toList());

        Page<SysReportDto> sysReportDtoPage = pageUtil.pageList(sysReportDtoList, req);
        return Result.success(sysReportDtoPage);
    }



    /**
     * 创建保存或更新的报到信息
     */
    @ApiOperation(value = "创建或更新报到信息", notes = "创建或更新报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysReport sysReport){

        /**
         * 查询是否存在
         */
        QueryWrapper<SysReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",sysReport.getUserId());
        int count = sysReportMapper.selectCount(queryWrapper);

        /**
         * 存报到信息
         */
        if(count == 0){
            sysReport.setCreated(LocalDateTime.now());
        }else{
            sysReport.setUpdated(LocalDateTime.now());
        }
        sysReportService.saveOrUpdate(sysReport,queryWrapper);
        return Result.success(sysReport);
    }

    /**
     * 更新报到信息
     */
    @ApiOperation(value = "更新报到信息", notes = "更新报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:update')")
    @PostMapping("/update")
//    @HoneyLogs(operation = "宿舍", type = "修改",url = "sys:dormitory:update")
    public Result update(@RequestBody SysReport sysReport){
        SysReport sysReport1 = sysReportService.getById(sysReport.getId());
        log.info("sysReport1 {}",sysReport1);
        log.info("sysReport {}",sysReport);
        sysReportService.updateById(sysReport);
        List<String> stringList = new ArrayList<>();
        stringList.add(sysReport1.toString());
        stringList.add(sysReport.toString());
        return Result.success(stringList);
    }

    /**
     * 删除信息
     */
    @ApiOperation(value = "删除报到信息", notes = "删除报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:delete')")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long id) {
        boolean flagLog = sysReportService.removeById(id);
        return Result.success(flagLog);
    }

    /**
     * 更新用户预报到状态
     */
    @ApiOperation(value = "更新预报到状态", notes = "更新报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:status')")
    @PostMapping("/status")
    public Result status(@RequestBody Long id){

        /**
         * 查询报到信息是否存在
         */
        QueryWrapper<SysReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        int count = sysReportMapper.selectCount(queryWrapper);

        /**
         * 存报到信息
         */
            SysReport sysReport = sysReportMapper.selectOne(queryWrapper);
            return Result.success(sysReport);

    }

    /**
     * 统计学院学生报道状态
     */
    @ApiOperation(value = "统计学院学生预报到状态", notes = "统计报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:count:institute')")
    @PostMapping("/count/institute")
    public Result countInstitute(@RequestBody SysInstituteClassNumber instituteNumber){
        /**
         * 查询学院学生报到信息
         */
        if(instituteNumber.getInstitute().equals("all") && instituteNumber.getClassNumber().equals("all")){
            int reportCount = sysReportService.count(null);
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生"));
            int inReportCount = userCount - reportCount;
            int[] count = {reportCount,inReportCount};
            return Result.success(Arrays.toString(count));
        }else if(instituteNumber.getClassNumber().equals("all") && !instituteNumber.getInstitute().equals("all")){
            int reportCount = sysUserService.countReportByInstitute(instituteNumber.getInstitute());
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生").eq("institute",instituteNumber.getInstitute()));
            int inReportCount = userCount - reportCount;
            int[] count = {reportCount,inReportCount};
            return Result.success(Arrays.toString(count));
        }else{
            int reportCount = sysUserService.countReportByClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生").eq("institute",instituteNumber.getInstitute()).eq("class_number",instituteNumber.getClassNumber()));
            int inReportCount = userCount - reportCount;
            int[] count = {reportCount,inReportCount};
            return Result.success(Arrays.toString(count));
        }
    }

    /**
     * 导出学院学生报道状态
     */
    @ApiOperation(value = "导出学院学生预报到状态", notes = "统计报到信息")
//    @PreAuthorize("hasAnyAuthority('sys:report:list:institute')")
    @PostMapping("/list/institute")
    public Result listInstitute(@RequestBody SysInstituteClassNumber instituteNumber){
        /**
         * 查询学院学生报到信息
         */
        if(instituteNumber.getInstitute().equals("all") && instituteNumber.getClassNumber().equals("all")){
            List<SysUser> sysUsers = sysUserService.getUserListByReport();
            List<SysReportUserDto> sysReportUserDtoList = copySysUser(sysUsers);
            return Result.success(sysReportUserDtoList);
        }else if(instituteNumber.getClassNumber().equals("all") && !instituteNumber.getInstitute().equals("all")){
            List<SysUser> sysUsers = sysUserService.getUserListByReportInstitute(instituteNumber.getInstitute());
            List<SysReportUserDto> sysReportUserDtoList = copySysUser(sysUsers);
            return Result.success(sysReportUserDtoList);
        }else{
            List<SysUser> sysUsers = sysUserService.getUserListByReportClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
            List<SysReportUserDto> sysReportUserDtoList = copySysUser(sysUsers);
            return Result.success(sysReportUserDtoList);
        }
    }

    public List<SysReportUserDto> copySysUser(List<SysUser> sysUsers){
        List<SysReportUserDto> sysReportUserDtoList = sysUsers.stream()
                .map(sysUser -> {
                    SysReportUserDto sysReportUserDto = new SysReportUserDto();
                    BeanUtils.copyProperties(sysUser, sysReportUserDto);
                    QueryWrapper<SysReport> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("user_id",sysReportUserDto.getId());
                    SysReport sysReport = sysReportMapper.selectOne(queryWrapper1);
                    BeanUtils.copyProperties(sysReport,sysReportUserDto);
                    return sysReportUserDto;
                })
                .collect(Collectors.toList());
        return sysReportUserDtoList;
    }

    /**
     * 导出未报道学生信息
     * @param request
     * @param response
     * @param sysInstituteClassNumber
     * @throws IOException
     */
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody SysInstituteClassNumber sysInstituteClassNumber) throws IOException {
        List<String> headList = Arrays.asList("用户名", "姓名","学院","专业","班级","电话","邮箱");
        List<SysUser> userList = new ArrayList<>();
        if (sysInstituteClassNumber != null) {
            if(sysInstituteClassNumber.getInstitute().equals("all") && sysInstituteClassNumber.getClassNumber().equals("all")){
                userList = sysUserService.getNotUserListByReport();
            }else if(sysInstituteClassNumber.getClassNumber().equals("all") && !sysInstituteClassNumber.getInstitute().equals("all")){
                userList = sysUserService.getNotUserListByReportInstitute(sysInstituteClassNumber.getInstitute());
            }else{
                userList = sysUserService.getNotUserListByReportClassNumber(sysInstituteClassNumber.getInstitute(),sysInstituteClassNumber.getClassNumber());
            }
        }
        if (!userList.isEmpty()) {
            userList.sort(
                    Comparator.comparing(SysUser::getInstitute, Comparator.nullsLast(String::compareTo)).reversed()
                            .thenComparing(Comparator.comparing(SysUser::getSpeciality, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getClassNumber, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getUsername, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getName, Comparator.nullsLast(String::compareTo)).reversed())
            );
        }
        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();

        // 转换SysUser数据为适用于导出的数据结构
        for (SysUser user : userList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUsername());
            map.put("姓名", user.getName());
            map.put("学院", user.getInstitute());
            map.put("专业", user.getSpeciality());
            map.put("电话", user.getPhone());
            map.put("邮箱", user.getEmail());
            dataList.add(map);
        }

        com.it2.springbootweb2.util.ExcelExportUtils.exportExcel(headList, dataList, "学生名单1", "某校学生名单", response);
    }
}
