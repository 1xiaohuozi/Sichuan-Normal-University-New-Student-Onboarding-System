package com.example.sys_newwelcome.mapper;

import com.example.sys_newwelcome.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sys_newwelcome.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  用户Mapper接口
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.mapper.SysUserMapper
 * @version:1.0
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据userId查询MenuId
     */
    List<Long> getNavMenuIds(Long userId);

    List<SysUser> listByMenuId(Long menuId);

    int getByUserIds(String institute);

    int getByUserIds2(String institute,String classNumber);

    int getByPaymentInstitute(String institute);

    int getByPassPaymentInstitute(String institute);

    int getByReviewPaymentInstitute(String institute);

    int getByPaymentClassNumber(String institute,String classNumber);

    int getByPassPaymentClassNumber(String institute,String classNumber);

    int getByReviewPaymentClassNumber(String institute,String classNumber);

    int getByChannelInstitute(String institute);

    int getByPassChannelInstitute(String institute);

    int getByReviewChannelInstitute(String institute);

    int getByChannelClassNumber(String institute,String classNumber);

    int getByPassChannelClassNumber(String institute,String classNumber);

    int getByReviewChannelClassNumber(String institute,String classNumber);

    List<SysUser> getUserByReport();

    List<SysUser> getUserByReportInstitute(String institute);

    List<SysUser> getUserByReportClassNumber(String institute,String classNumber);

    List<SysUser> getPassOrReviewUserByPayment(int statu);

    List<SysUser> getNotUserByPayment();

    List<SysUser> getPassOrReviewUserByPaymentInstitute(String institute,int statu);

    List<SysUser> getNotUserByPaymentInstitute(String institute);

    List<SysUser> getPassOrReviewUserByPaymentClassNumber(String institute,String classNumber,int statu);

    List<SysUser> getNotUserByPaymentClassNumber(String institute,String classNumber);

    List<SysUser> getPassOrReviewUserByChannel(int statu);

    List<SysUser> getNotUserByChannel();

    List<SysUser> getPassOrReviewUserByChannelInstitute(String institute,int statu);

    List<SysUser> getNotUserByChannelInstitute(String institute);

    List<SysUser> getPassOrReviewUserByChannelClassNumber(String institute,String classNumber,int statu);

    List<SysUser> getNotUserByChannelClassNumber(String institute,String classNumber);

    List<SysUser> getDormitoryByInstitute(String institute);

    List<SysUser> getNotUserByReport();

    List<SysUser> getNotUserByReportInstitute(String institute);

    List<SysUser> getNotUserByReportClassNumber(String institute,String classNumber);

}
