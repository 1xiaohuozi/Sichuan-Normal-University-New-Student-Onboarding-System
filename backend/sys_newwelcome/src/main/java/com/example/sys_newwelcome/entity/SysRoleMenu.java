package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 角色菜单权限实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysRoleMenu
 * @version:1.0
 */
@Data
@TableName("sys_role_menu")
@ApiModel(value = "SysRoleMenu", description = "角色菜单权限实体类")
public class SysRoleMenu{

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "角色菜单权限ID", example = "1")
    private Long id;

    @ExcelProperty("角色ID")
    @ApiModelProperty(value = "角色ID", example = "2")
    private Long roleId;

    @ExcelProperty("菜单ID")
    @ApiModelProperty(value = "菜单ID", example = "3")
    private Long menuId;
}
