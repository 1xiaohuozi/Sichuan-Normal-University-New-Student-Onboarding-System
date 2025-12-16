package com.example.sys_newwelcome.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sys_newwelcome.common.dto.SysMenuDto;
import com.example.sys_newwelcome.entity.SysMenu;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysMenuMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys_newwelcome.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.impl.SysMenuServiceImpl
 * @version:1.0
 */
@Service
/**
 * 日志记录器
 */
@Slf4j
/**
 * 使用 SysMenuMapper 进行数据库访问，而 SysMenu 是与数据库表对应的实体对象
 */
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 获取当前用户的导航栏
     */
    @Override
    public List<SysMenuDto> getCurrentUserNav(){
        /**
         * 从SpringSecurity中获取当前已认证的用户名
         * 用户登录后的用户名
         */
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /**
         * 根据用户名获取用户相关信息
         */
        SysUser sysUser = sysUserService.getByUserName(username);
        /**
         * 获取用户的导航栏标识ID
         */
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        /**
         * 根据id检索相应的菜单数据
         */
        List<SysMenu> menus = this.listByIds(menuIds);
        /**
         * 调用buildTreeMenu方法构建一个树形结构的菜单
         * 将所有的菜单按照其父子关系组织在一起
         */
        List<SysMenu> menuTree = buildTreeMenu(menus);
        /**
         * 将树形菜单结构转换为SysMenuDto对象的列表
         */
        return convert(menuTree);
    }

    /**
     * 构建树形结构的菜单列表
     */
    private List<SysMenu> buildTreeMenu(List<SysMenu> menus){
        /**
         * 建空列表
         */
        List<SysMenu> finalMenus = new ArrayList<>();
        /**
         * 迭代每个SysMenu对象嵌套循环
         */
        for(SysMenu menu:menus) {
            /**
             * 再次循环menus列表查查找具有父子关系的菜单项
             */
            for (SysMenu e : menus) {
                /**
                 * 当前迭代的menu对象的ID与内部循环中得到e的parentId属性相等就将e添加为menu对象的子菜单
                 */
                if (menu.getId().equals(e.getParentId())) {
                    menu.getChildren().add(e);
                }
            }
            /**
             * 如果parentId属性等于0则到了顶级菜单，则将他添加到最终的菜单列表finalMenus中
             */
            if (menu.getParentId() == 0) {
                finalMenus.add(menu);
            }
        }
        /**
         * 日志记录器将finalMenus列表以JSON格式的字符串打印出来
         */
        log.info(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }

    /**
     * 将树形结构SysMenu对象转换为SysMenuDto对象的列表
     */
    private List<SysMenuDto> convert(List<SysMenu> menuTree){
        /**
         * 创建空SysMenuDto
         */
        List<SysMenuDto> menuDtos = new ArrayList<>();
        /**
         * forEach方法遍历传入menuTree
         */
        menuTree.forEach(m ->{
            /**
             * 创建一个新的SysMenuDto对象Dto
             */
            SysMenuDto dto = new SysMenuDto();
            /**
             * 从m中获取属性传入到dto中
             */
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setIcon(m.getIcon());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            /**
             * 检查是否有子菜单
             */
            if(m.getChildren().size() > 0){
                /**
                 * 有子菜单也转换成SysMenuDto对象并将它们设置为dto的children属性
                 */
                dto.setChildren(convert(m.getChildren()));
            }
            /**
             * 将Dto对象添加到menuDtos列表中
             */
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    @Override
    public List<SysMenu> tree(){
        /**
         * 获取所有菜单信息
         */
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        /**
         * 转成树状结构
         */
        return buildTreeMenu(sysMenus);
    }
}
