package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.sys_newwelcome.entity.SysDormitory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDormitoryDto extends SysDormitory {
    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名", example = "某某某")
    private String username;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "姓名", example = "John Doe")
    private String name;

    @ExcelProperty("学院")
    @ApiModelProperty(value = "学院", example = "计算机科学学院")
    private String institute;

    @ExcelProperty("专业")
    @ApiModelProperty(value = "专业", example = "软件工程")
    private String speciality;

    @ExcelProperty("班级")
    @ApiModelProperty(value = "班级", example = "2021级3班")
    private String classNumber;
}
