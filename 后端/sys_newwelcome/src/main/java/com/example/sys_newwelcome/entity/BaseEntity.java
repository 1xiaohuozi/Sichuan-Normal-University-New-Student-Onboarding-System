package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 *  基本实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.BaseEntity
 * @version:1.0
 */
@Data
@ApiModel(value = "BaseEntity", description = "基本实体类")
public class BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty("ID")
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @ExcelProperty("创建时间")
    @ApiModelProperty(value = "创建时间", example = "2023-10-30T10:00:00")
    private LocalDateTime created;

    @ExcelProperty("更新时间")
    @ApiModelProperty(value = "更新时间", example = "2023-10-30T11:00:00")
    private LocalDateTime updated;

    @ExcelProperty("状态")
    @ApiModelProperty(value = "状态", example = "1")
    private Integer statu;
}
