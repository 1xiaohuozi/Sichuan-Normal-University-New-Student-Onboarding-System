package com.example.sys_newwelcome.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.dto.*;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysPayment;
import com.example.sys_newwelcome.entity.SysReport;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysPaymentMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysPaymentService;
import com.example.sys_newwelcome.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "缴费信息管理", tags = "缴费信息接口")
@RequestMapping("/sys/payment")
public class SysPaymentController extends BaseController{

    @Autowired
    SysPaymentMapper sysPaymentMapper;
    @Autowired
    SysPaymentService sysPaymentService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtils pageUtil;

    /**
     * 获取缴费信息列表
     */
    @ApiOperation(value = "获取缴费信息列表列表", notes = "根据用户username获取缴费信息列表")
    @GetMapping("/list")
    public Result getPaymentList(){
        List<SysPayment> sysPaymentList = sysPaymentMapper.selectList(null);
        List<SysPaymentDto> sysPaymentDtoList = sysPaymentList.stream()
                .map(sysPayment -> {
                    SysPaymentDto sysPaymentDto = new SysPaymentDto();
                    /**
                     * 将 SysPayment 中的属性复制到 SysPaymentDto
                     */
                    BeanUtils.copyProperties(sysPayment, sysPaymentDto);
                    QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("id",sysPayment.getUserId());
                    SysUser sysUser = sysUserMapper.selectOne(queryWrapper1);
                    if(sysPaymentDto != null){
                        sysPaymentDto.setName(sysUser.getName());
                        sysPaymentDto.setUsername(sysUser.getUsername());
                    }
                    return sysPaymentDto;
                })
                .collect(Collectors.toList());

        Page<SysPaymentDto> sysPaymentDtoPage = pageUtil.pageList(sysPaymentDtoList,req);
        return Result.success(sysPaymentDtoPage);
    }

    /**
     * 根据userId获取缴费信息
     */
    @ApiOperation(value = "获取缴费信息", notes = "根据userId获取缴费信息")
    @PostMapping("/info")
    public Result getPayment(@RequestBody Long id){
        /**
         * 查询userId对应缴费信息
         */
        QueryWrapper<SysPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        SysPayment sysPayment = sysPaymentMapper.selectOne(queryWrapper);
        return Result.success(sysPayment);
    }


    /**
     * 创建保存或更新的缴费信息
     */
    @ApiOperation(value = "创建或更新缴费信息", notes = "创建或更新缴费信息信息")
//    @PreAuthorize("hasAnyAuthority('sys:payment:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysPayment sysPayment){

        /**
         * 查询是否存在
         */
        QueryWrapper<SysPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",sysPayment.getUserId());
        int count = sysPaymentMapper.selectCount(queryWrapper);

        /**
         * 存缴费信息
         */
        if(count == 0){
            sysPayment.setCreated(LocalDateTime.now());
        }else{
            sysPayment.setUpdated(LocalDateTime.now());
        }
        sysPayment.setStatu(1);
        sysPaymentService.saveOrUpdate(sysPayment,queryWrapper);
        return Result.success(sysPayment);
    }


    /**
     * 更新用户缴费状态状态
     */
    @ApiOperation(value = "更新缴费状态", notes = "更新缴费状态")
//    @PreAuthorize("hasAnyAuthority('sys:payment:status')")
    @PostMapping("status")
    public Result status(@RequestBody Long id){

        /**
         * 查询是否存在
         */
        QueryWrapper<SysPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        /**
         * 存状态
         */
        SysPayment sysPayment = sysPaymentMapper.selectOne(queryWrapper);
        sysPayment.setStatu(2);
        sysPaymentService.update(sysPayment,queryWrapper);
        return Result.success(sysPayment);
    }


    /**
     * 审核不通过
     */
    @ApiOperation(value = "更新缴费状态", notes = "更新缴费状态")
//    @PreAuthorize("hasAnyAuthority('sys:payment:nopass')")
    @PostMapping("/nopass")
    public Result nopass(@RequestBody Long id){

        /**
         * 查询id对应缴费信息
         */
        QueryWrapper<SysPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        /**
         * 存状态
         */
        SysPayment sysPayment = sysPaymentMapper.selectOne(queryWrapper);
        sysPayment.setStatu(3);
        sysPaymentService.update(sysPayment,queryWrapper);
        return Result.success(sysPayment);
    }

    /**
     * 统计学院学生缴费状态
     */
    @ApiOperation(value = "统计学院学生缴费状态", notes = "统计缴费信息")
//    @PreAuthorize("hasAnyAuthority('sys:payment:count:institute')")
    @PostMapping("/count/institute")
    public Result countInstitute(@RequestBody SysInstituteClassNumber instituteNumber){
        /**
         * 查询学院、班级学生缴费信息
         */
        if(instituteNumber.getInstitute().equals("all") && instituteNumber.getClassNumber().equals("all")){
            int passCount = sysPaymentService.count(new QueryWrapper<SysPayment>().eq("statu",2));
            int reviewCount = sysPaymentService.count(new QueryWrapper<SysPayment>().eq("statu",1));
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生"));
            int notCount= userCount - passCount - reviewCount;
            int[] count = {reviewCount,passCount,notCount};
            return Result.success(Arrays.toString(count));
        }else if(instituteNumber.getClassNumber().equals("all") && !instituteNumber.getInstitute().equals("all")){
            int passCount = sysUserService.countPassPaymentByInstitute(instituteNumber.getInstitute());
            int reviewCount = sysUserService.countReviewPaymentByInstitute(instituteNumber.getInstitute());
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生")
                    .eq("institute",instituteNumber.getInstitute())
            );
            System.out.println(userCount);
            System.out.println(reviewCount);
            System.out.println(passCount);
            int notCount= userCount - passCount - reviewCount;
            int[] count = {reviewCount,passCount,notCount};
            return Result.success(Arrays.toString(count));
        }else{
            int passCount = sysUserService.countPassPaymentByClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
            int reviewCount = sysUserService.countReviewPaymentByClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生")
                    .eq("institute",instituteNumber.getInstitute())
                    .eq("class_number",instituteNumber.getClassNumber())
            );
            int notCount= userCount - passCount - reviewCount;
            int[] count = {reviewCount,passCount,notCount};
            return Result.success(Arrays.toString(count));
        }
    }

    /**
     * 导出学院学生缴费状态信息
     */
    @ApiOperation(value = "导出学院学生缴费状态信息", notes = "统计缴费信息")
//    @PreAuthorize("hasAnyAuthority('sys:payment:list:institute')")
    @PostMapping("/list/institute")
    public Result listInstitute(@RequestBody SysICNStatu sysICNStatu){
        /**
         * 查询学院学生缴费信息
         */
        if(sysICNStatu.getInstitute().equals("all") && sysICNStatu.getClassNumber().equals("all")){
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByPayment(sysICNStatu.getStatu());
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByPayment();
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }
        }else if(sysICNStatu.getClassNumber().equals("all") && !sysICNStatu.getInstitute().equals("all")){
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByPaymentInstitute(sysICNStatu.getInstitute(),sysICNStatu.getStatu());
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByPaymentInstitute(sysICNStatu.getInstitute());
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }
        }else{
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByPaymentClassNumber(sysICNStatu.getInstitute(),sysICNStatu.getClassNumber(),sysICNStatu.getStatu());
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByPaymentClassNumber(sysICNStatu.getInstitute(),sysICNStatu.getClassNumber());
                List<SysPaymentUserDto> sysPaymentUserDtoList = copySysUser(sysUsers);
                return Result.success(sysPaymentUserDtoList);
            }
        }
    }

    public List<SysPaymentUserDto> copySysUser(List<SysUser> sysUsers){
        List<SysPaymentUserDto> sysPaymentUserDtoList = sysUsers.stream()
                .map(sysUser -> {
                    SysPaymentUserDto sysPaymentUserDto = new SysPaymentUserDto();
                    BeanUtils.copyProperties(sysUser, sysPaymentUserDto);
                    return sysPaymentUserDto;
                })
                .collect(Collectors.toList());
        return sysPaymentUserDtoList;
    }

    /**
     * 导出未缴费学生信息
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
                userList = sysUserService.getNotUserListByPayment();
            }else if(sysInstituteClassNumber.getClassNumber().equals("all") && !sysInstituteClassNumber.getInstitute().equals("all")){
                userList = sysUserService.getNotUserListByPaymentInstitute(sysInstituteClassNumber.getInstitute());
            }else{
                userList = sysUserService.getNotUserListByPaymentClassNumber(sysInstituteClassNumber.getInstitute(),sysInstituteClassNumber.getClassNumber());
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
            map.put("班级", user.getClassNumber());
            map.put("电话", user.getPhone());
            map.put("邮箱", user.getEmail());
            dataList.add(map);
        }

        com.it2.springbootweb2.util.ExcelExportUtils.exportExcel(headList, dataList, "学生名单1", "某校学生名单", response);
    }
}
