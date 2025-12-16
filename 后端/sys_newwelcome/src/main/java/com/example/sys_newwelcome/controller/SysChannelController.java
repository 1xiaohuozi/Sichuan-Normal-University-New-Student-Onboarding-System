package com.example.sys_newwelcome.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.dto.*;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysChannel;
import com.example.sys_newwelcome.entity.SysPayment;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysChannelMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysChannelService;
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
@Api(value = "绿色通道管理", tags = "绿色通道管理接口")
@RequestMapping("/sys/channel")
public class SysChannelController extends BaseController{


    @Autowired
    SysChannelMapper sysChannelMapper;
    @Autowired
    SysChannelService sysChannelService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtils pageUtil;

    /**
     * 获取贫困申请信息列表
     */
    @ApiOperation(value = "获取贫困申请信息列表列表", notes = "根据用户username获取贫困申请信息列表")
    @GetMapping("/list")
    public Result getChannelList(){
        List<SysChannel> sysChannelList = sysChannelMapper.selectList(null);
        List<SysChannelDto> sysChannelDtoList = sysChannelList.stream()
                .map(sysChannel -> {
                    SysChannelDto sysChannelDto = new SysChannelDto();
                    /**
                     * 将 SysChannel 中的属性复制到 SysChannelDto
                     */
                    BeanUtils.copyProperties(sysChannel, sysChannelDto);
                    QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("id",sysChannel.getUserId());
                    SysUser sysUser = sysUserMapper.selectOne(queryWrapper1);
                    sysChannelDto.setName(sysUser.getName());
                    sysChannelDto.setUsername(sysUser.getUsername());
                    return sysChannelDto;
                })
                .collect(Collectors.toList());

        Page<SysChannelDto> sysChannelDtoPage = pageUtil.pageList(sysChannelDtoList,req);
        return Result.success(sysChannelDtoPage);
    }

    /**
     * 根据userId获取贫困申请信息
     */
    @ApiOperation(value = "获取贫困申请信息", notes = "根据userId获取贫困申请信息")
    @PostMapping("/info")
    public Result getChannel(@RequestBody Long id){
        /**
         * 查询userId对应缴费信息
         */
        QueryWrapper<SysChannel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        SysChannel sysChannel = sysChannelMapper.selectOne(queryWrapper);
        return Result.success(sysChannel);
    }

    /**
     * 创建保存或更新的贫困信息
     */
    @ApiOperation(value = "创建或更新贫困申请信息", notes = "创建或更新贫困申请信息信息")
//    @PreAuthorize("hasAnyAuthority('sys:channel:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysChannel sysChannel){

        /**
         * 查询是否存在
         */
        QueryWrapper<SysChannel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",sysChannel.getUserId());
        int count = sysChannelMapper.selectCount(queryWrapper);

        /**
         * 存贫困申请信息
         */
        if(count == 0){
            sysChannel.setCreated(LocalDateTime.now());
        }else{
            sysChannel.setUpdated(LocalDateTime.now());
        }
        sysChannel.setStatu(1);
        sysChannelService.saveOrUpdate(sysChannel,queryWrapper);
        return Result.success(sysChannel);
    }

    /**
     * 用户贫困申请状态审核通过
     */
    @ApiOperation(value = "更新贫困信息状态", notes = "更新贫困信息状态")
//    @PreAuthorize("hasAnyAuthority('sys:channel:status')")
    @PostMapping("/status")
    public Result status(@RequestBody Long id){

        /**
         * 查询id对应贫困信息
         */
        QueryWrapper<SysChannel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        /**
         * 存状态
         */
        SysChannel sysChannel = sysChannelMapper.selectOne(queryWrapper);
        sysChannel.setStatu(2);
        sysChannelService.update(sysChannel,queryWrapper);
        return Result.success(sysChannel);
    }


    /**
     * 审核不通过
     */
    @ApiOperation(value = "更新贫困信息状态", notes = "更新贫困信息状态")
//    @PreAuthorize("hasAnyAuthority('sys:Channel:nopass')")
    @PostMapping("/nopass")
    public Result nopass(@RequestBody Long id){

        /**
         * 查询id对应贫困信息
         */
        QueryWrapper<SysChannel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        /**
         * 存状态
         */
        SysChannel sysChannel = sysChannelMapper.selectOne(queryWrapper);
        sysChannel.setStatu(3);
        sysChannelService.update(sysChannel,queryWrapper);
        return Result.success(sysChannel);
    }


    /**
     * 统计学院学生绿色通道申请状态
     */
    @ApiOperation(value = "统计学院学生绿色通道申请状态", notes = "统计绿色通道申请信息")
//    @PreAuthorize("hasAnyAuthority('sys:channel:count:institute')")
    @PostMapping("/count/institute")
    public Result countInstitute(@RequestBody SysInstituteClassNumber instituteNumber){
        /**
         * 查询学院、班级学生绿色通道信息
         */
        System.out.println(instituteNumber);
        if(instituteNumber.getInstitute().equals("all") && instituteNumber.getClassNumber().equals("all")){
            int passCount = sysChannelService.count(new QueryWrapper<SysChannel>().eq("statu",2));
            int reviewCount = sysChannelService.count(new QueryWrapper<SysChannel>().eq("statu",1));
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生"));
            int notCount= userCount - passCount - reviewCount;
            int[] count = {reviewCount,passCount,notCount};
            return Result.success(Arrays.toString(count));
        }else if(instituteNumber.getClassNumber().equals("all") && !instituteNumber.getInstitute().equals("all")){
            int passCount = sysUserService.countPassChannelByInstitute(instituteNumber.getInstitute());
            int reviewCount = sysUserService.countReviewChannelByInstitute(instituteNumber.getInstitute());
            int userCount = sysUserService.count(new QueryWrapper<SysUser>().eq("duties","学生")
                    .eq("institute",instituteNumber.getInstitute())
            );
            int notCount= userCount - passCount - reviewCount;
            int[] count = {reviewCount,passCount,notCount};
            return Result.success(Arrays.toString(count));
        }else{
            int passCount = sysUserService.countPassChannelByClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
            int reviewCount = sysUserService.countReviewChannelByClassNumber(instituteNumber.getInstitute(),instituteNumber.getClassNumber());
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
     * 导出学院学生绿色通道申请状态信息
     */
    @ApiOperation(value = "导出学院学生绿色通道申请状态信息", notes = "统计绿色通道申请信息")
//    @PreAuthorize("hasAnyAuthority('sys:channel:list:institute')")
    @PostMapping("/list/institute")
    public Result listInstitute(@RequestBody SysICNStatu sysICNStatu){
        /**
         * 查询学院学生绿色通道申请信息
         */
        if(sysICNStatu.getInstitute().equals("all") && sysICNStatu.getClassNumber().equals("all")){
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByChannel(sysICNStatu.getStatu());
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByChannel();
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }
        }else if(sysICNStatu.getClassNumber().equals("all") && !sysICNStatu.getInstitute().equals("all")){
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByChannelInstitute(sysICNStatu.getInstitute(),sysICNStatu.getStatu());
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByChannelInstitute(sysICNStatu.getInstitute());
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }
        }else{
            if(sysICNStatu.getStatu() == 2 || sysICNStatu.getStatu() == 1){
                List<SysUser> sysUsers = sysUserService.getPassOrReviewUserListByChannelClassNumber(sysICNStatu.getInstitute(),sysICNStatu.getClassNumber(),sysICNStatu.getStatu());
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }else{
                List<SysUser> sysUsers = sysUserService.getNotUserListByChannelClassNumber(sysICNStatu.getInstitute(),sysICNStatu.getClassNumber());
                List<SysChannelUserDto> sysChannelUserDtoList = copySysUser(sysUsers);
                return Result.success(sysChannelUserDtoList);
            }
        }
    }

    public List<SysChannelUserDto> copySysUser(List<SysUser> sysUsers){
        List<SysChannelUserDto> sysChannelUserDtoList = sysUsers.stream()
                .map(sysUser -> {
                    SysChannelUserDto sysChannelUserDto = new SysChannelUserDto();
                    BeanUtils.copyProperties(sysUser, sysChannelUserDto);
                    return sysChannelUserDto;
                })
                .collect(Collectors.toList());
        return sysChannelUserDtoList;
    }

    /**
     * 导出申请绿色通道通过学生信息
     * @param request
     * @param response
     * @param sysInstituteClassNumber
     * @throws IOException
     */
    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestBody SysInstituteClassNumber sysInstituteClassNumber) throws IOException {
        List<String> headList = Arrays.asList("用户名", "姓名","学院","专业","班级","电话","邮箱","身份证");//表头
        List<SysUser> userList = new ArrayList<>();//用于存储从数据库中查询到的SysUser对象列表，这些对象包含了学生的信息。
        if (sysInstituteClassNumber != null) {
            if(sysInstituteClassNumber.getInstitute().equals("all") && sysInstituteClassNumber.getClassNumber().equals("all")){
                userList = sysUserService.getPassOrReviewUserListByChannel(2);
            }else if(sysInstituteClassNumber.getClassNumber().equals("all") && !sysInstituteClassNumber.getInstitute().equals("all")){
                userList = sysUserService.getPassOrReviewUserListByChannelInstitute(sysInstituteClassNumber.getInstitute(),2);
            }else{
                userList = sysUserService.getPassOrReviewUserListByChannelClassNumber(sysInstituteClassNumber.getInstitute(),sysInstituteClassNumber.getClassNumber(),2);
            }
        }
        //排序，如果学生信息列表不为空，对学生信息列表进行多条件排序，按照学院、专业、班级、用户名、姓名的顺序降序排列。
        if (!userList.isEmpty()) {
            userList.sort(
                    Comparator.comparing(SysUser::getInstitute, Comparator.nullsLast(String::compareTo)).reversed()
                            .thenComparing(Comparator.comparing(SysUser::getSpeciality, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getClassNumber, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getUsername, Comparator.nullsLast(String::compareTo)).reversed())
                            .thenComparing(Comparator.comparing(SysUser::getName, Comparator.nullsLast(String::compareTo)).reversed())
            );
        }
        //数据转换
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
            map.put("身份证", user.getIdCard());
            dataList.add(map);
        }

        com.it2.springbootweb2.util.ExcelExportUtils.exportExcel(headList, dataList, "学生名单1", "某校学生名单", response);
    }
}
