package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysMenu
 * @version:1.0
 */
@Data
/**
 * 子类继承父类字段equals、hashCode方法
 */
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysMenu", description = "菜单实体类")
public class SysMenu extends BaseEntity {
    /**
     * 确保在类的结构没有重大变化的情况下，即使类发生了一些修改，仍然能够正确地进行对象序列化和反序列化。这有助于确保向后兼容性和避免不必要的序列化问题
     */
    private static final long serialVersionUID = 1L;

    /**
     * 一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    @ExcelProperty("父菜单ID")
    @ApiModelProperty(value = "父菜单ID", example = "1")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @ExcelProperty("菜单名称")
    @ApiModelProperty(value = "菜单名称", example = "示例菜单")
    private String name;

    @ExcelProperty("菜单URL")
    @ApiModelProperty(value = "菜单URL", example = "/example")
    private String path;

    @NotBlank(message = "菜单授权码不能为空")
    @ExcelProperty("菜单权限")
    @ApiModelProperty(value = "菜单权限", example = "menu:example")
    private String perms;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注", example = "示例备注")
    private String component;

    /**
     * 0:目录 1:菜单 2:按钮
     */
    @NotNull(message = "菜单类型不能为空")
    @ExcelProperty("菜单类型")
    @ApiModelProperty(value = "菜单类型", example = "1")
    private Integer type;

    @ExcelProperty("菜单图标")
    @ApiModelProperty(value = "菜单图标", example = "icon-example")
    private String icon;

    @ExcelProperty("排序")
    @TableField("orderNum")
    @ApiModelProperty(value = "排序", example = "1")
    private Integer ordernum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
