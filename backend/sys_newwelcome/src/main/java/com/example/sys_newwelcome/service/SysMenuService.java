package com.example.sys_newwelcome.service;

import com.example.sys_newwelcome.common.dto.SysMenuDto;
import com.example.sys_newwelcome.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.SysMenuService
 * @version:1.0
 */

/**
 * 更高级别的业务逻辑（对数据库管理操作）
 */

/**
 * 提供与系统菜单（SysMenu）相关的业务逻辑方法
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取导航菜单信息
     */
    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> tree();
}
