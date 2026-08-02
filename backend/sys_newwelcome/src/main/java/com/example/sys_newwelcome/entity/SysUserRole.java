package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysUserRole
 * @version:1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
@ApiModel(value = "SysUserRole", description = "用户角色实体类")
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @ExcelProperty("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty("角色ID")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}
