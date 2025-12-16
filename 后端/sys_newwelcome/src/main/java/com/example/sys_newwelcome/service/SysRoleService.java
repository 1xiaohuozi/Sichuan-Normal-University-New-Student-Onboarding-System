package com.example.sys_newwelcome.service;

import com.example.sys_newwelcome.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.SysRoleService
 * @version:1.0
 */
/**
 * 提供与系统角色（SysRole）相关的业务逻辑方法
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listRolesByUserId(Long id);
}
