package com.example.sys_newwelcome.service.impl;

import com.example.sys_newwelcome.entity.SysUserRole;
import com.example.sys_newwelcome.mapper.SysUserRoleMapper;
import com.example.sys_newwelcome.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.impl.SysUserRoleServiceImpl
 * @version:1.0
 */
/**
 * 使用 SysUserRoleMapper 进行数据库访问，而 SysUserRole 是与数据库表对应的实体对象
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
