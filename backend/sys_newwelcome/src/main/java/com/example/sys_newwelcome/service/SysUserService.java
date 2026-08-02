package com.example.sys_newwelcome.service;

import com.example.sys_newwelcome.common.email.param.LoginParam;
import com.example.sys_newwelcome.common.email.vo.R;
import com.example.sys_newwelcome.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.SysUserService
 * @version:1.0
 */
/**
 * 提供与系统用户（SysUser）相关的业务逻辑方法
 */
public interface SysUserService extends IService<SysUser> {
    R findPassword(LoginParam loginParam);
    /**
     * 根据用户名检索用户信息
     */
    SysUser getByUserName(String username);

    /**
     * 根据用户userId查询权限信息
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 清理缓存
     */
    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);
    int countReportByInstitute(String institute);
    int countReportByClassNumber(String institute,String classNumber);
    int countPassPaymentByInstitute(String institute);
    int countReviewPaymentByInstitute(String institute);
    int countPassPaymentByClassNumber(String institute,String classNumber);
    int countReviewPaymentByClassNumber(String institute,String classNumber);
    int countPassChannelByInstitute(String institute);
    int countReviewChannelByInstitute(String institute);
    int countPassChannelByClassNumber(String institute,String classNumber);
    int countReviewChannelByClassNumber(String institute,String classNuymber);
    List<SysUser> getUserListByReport();
    List<SysUser> getUserListByReportInstitute(String institute);
    List<SysUser> getUserListByReportClassNumber(String institute,String classNumber);
    List<SysUser> getPassOrReviewUserListByPayment(int statu);
    List<SysUser> getNotUserListByPayment();
    List<SysUser> getPassOrReviewUserListByPaymentInstitute(String institute,int statu);
    List<SysUser> getNotUserListByPaymentInstitute(String institute);
    List<SysUser> getPassOrReviewUserListByPaymentClassNumber(String institute,String classNumber,int statu);
    List<SysUser> getNotUserListByPaymentClassNumber(String institute,String classNumber);
    List<SysUser> getPassOrReviewUserListByChannel(int statu);
    List<SysUser> getNotUserListByChannel();
    List<SysUser> getPassOrReviewUserListByChannelInstitute(String institute,int statu);
    List<SysUser> getNotUserListByChannelInstitute(String institute);
    List<SysUser> getPassOrReviewUserListByChannelClassNumber(String institute,String classNumber,int statu);
    List<SysUser> getNotUserListByChannelClassNumber(String institute,String classNumber);
    List<SysUser> getDormitoryListByInstitute(String institute);
    List<SysUser> getNotUserListByReport();
    List<SysUser> getNotUserListByReportInstitute(String institute);
    List<SysUser> getNotUserListByReportClassNumber(String institute,String classNumber);
}
