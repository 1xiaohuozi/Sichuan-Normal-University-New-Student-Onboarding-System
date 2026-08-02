package com.example.sys_newwelcome.mapper;

import com.example.sys_newwelcome.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  菜单Mapper接口
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.mapper.SysMenuMapper
 * @version:1.0
 */

/**
 *  继承数据库操作的通用的方法
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
