package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.sys_newwelcome.entity.SysRole;
import com.example.sys_newwelcome.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;

public class SysUserDto extends SysUser {
    @ExcelProperty("角色信息")
    @ApiModelProperty(value = "角色信息", example = "")
    private SysRole[] sysRole;
}
