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
 *  公告实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysNotice
 * @version:1.0
 */
@Data
@TableName("sys_notice")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysNotice", description = "公告实体类")
public class SysNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "公告标题不能为空")
    @ExcelProperty("公告标题")
    @ApiModelProperty(value = "公告标题", example = "示例标题")
    private String title;

    @NotBlank(message = "公告内容不能为空")
    @ExcelProperty("公告内容")
    @ApiModelProperty(value = "公告内容", example = "示例内容")
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private String content;

    @NotBlank(message = "作者不能为空")
    @ExcelProperty("作者")
    @ApiModelProperty(value = "作者", example = "示例作者")
    private String author;

    @TableField(exist = false)
    private List<SysNotice> notices = new ArrayList<>();
}
