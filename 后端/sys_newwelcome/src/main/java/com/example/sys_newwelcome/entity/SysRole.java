package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  角色实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysRole
 * @version:1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@ApiModel(value = "SysRole", description = "角色实体类")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名称不能为空")
    @ExcelProperty("角色名称")
    @ApiModelProperty(value = "角色名称", example = "示例角色")
    private String name;

    @NotBlank(message = "角色编码不能为空")
    @ExcelProperty("角色编码")
    @ApiModelProperty(value = "角色编码", example = "ROLE_EXAMPLE")
    private String code;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注", example = "示例备注")
    private String remark;

    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>();
}
