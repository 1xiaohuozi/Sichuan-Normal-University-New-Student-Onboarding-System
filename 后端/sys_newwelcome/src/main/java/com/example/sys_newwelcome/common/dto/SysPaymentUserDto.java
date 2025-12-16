package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SysPaymentUserDto {
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

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ExcelProperty("邮箱")
    @ApiModelProperty(value = "邮箱", example = "john@example.com")
    private String email;

    @ExcelProperty("民族")
    @ApiModelProperty(value = "民族", example = "汉族")
    private String nation;

    @ExcelProperty("身份证")
    @ApiModelProperty(value = "身份证", example = "510121XXXXXXXXXXX")
    private String idCard;

}
