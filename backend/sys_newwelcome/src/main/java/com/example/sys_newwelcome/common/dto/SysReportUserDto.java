package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysReportUserDto {
    @ExcelProperty("ID")
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名", example = "john_doe")
    private String username;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "姓名", example = "John Doe")
    private String name;

    @ExcelProperty("性别")
    @ApiModelProperty(value = "性别", example = "男")
    private String sex;

    @ExcelProperty("电话")
    @ApiModelProperty(value = "电话", example = "11234567890")
    private String phone;

    @ExcelProperty("学院")
    @ApiModelProperty(value = "学院", example = "计算机科学学院")
    private String institute;

    @ExcelProperty("专业")
    @ApiModelProperty(value = "专业", example = "软件工程")
    private String speciality;

    @ExcelProperty("班级")
    @ApiModelProperty(value = "班级", example = "2021级3班")
    private String classNumber;

    @ExcelProperty("预计到达事件")
    @ApiModelProperty(value = "预计到达时间", example = "2023-10-09")
    private String arrivalTime ;

    @ExcelProperty("交通方式")
    @ApiModelProperty(value = "交通方式", example = "飞机")
    private String transportation;

    @ExcelProperty("随行成员数量")
    @ApiModelProperty(value = "随行成员数量", example = "2")
    private Integer members;

    @ExcelProperty("备注")
    @ApiModelProperty(value = "备注", example = "无")
    private String notes;
}
