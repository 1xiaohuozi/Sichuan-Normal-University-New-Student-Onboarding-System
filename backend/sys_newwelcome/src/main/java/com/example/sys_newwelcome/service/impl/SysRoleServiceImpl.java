package com.example.sys_newwelcome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sys_newwelcome.entity.SysRole;
import com.example.sys_newwelcome.mapper.SysRoleMapper;
import com.example.sys_newwelcome.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.impl.SysRoleServiceImpl
 * @version:1.0
 */
/**
 * 使用 SysRoleMapper 进行数据库访问，而 SysRole 是与数据库表对应的实体对象
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    /**
     * 查询该用户的角色列表
     */
    @Override
    public List<SysRole> listRolesByUserId(Long userId){
        List<SysRole> sysRoleList = this.list(new QueryWrapper<SysRole>().inSql("id","select role_id from sys_user_role where user_id = " + userId));
        return sysRoleList;
    }


}
