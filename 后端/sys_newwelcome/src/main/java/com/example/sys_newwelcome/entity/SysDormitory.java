package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("sys_dormitory")
@ApiModel(value = "SysDormitory", description = "宿舍实体类")
public class SysDormitory extends BaseEntity{
    @ExcelProperty("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty("校区")
    @ApiModelProperty(value = "成龙校区")
    private String campus;

    @ExcelProperty("区域")
    @ApiModelProperty(value = "西苑")
    private String region;

    @ExcelProperty("寝室楼")
    @ApiModelProperty(value = "四幢")
    private String dormitory;

    @ExcelProperty("寝室号")
    @ApiModelProperty(value = "319")
    private String dormitoryNumber;

    @ExcelProperty("床位号")
    @ApiModelProperty(value = "1")
    private String bedNumber;
}
