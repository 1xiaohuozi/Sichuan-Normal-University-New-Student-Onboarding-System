package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName("sys_payment")
@ApiModel(value = "SysReport", description = "报道信息实体类类")
public class SysPayment extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ExcelProperty("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty("缴费凭证")
    @ApiModelProperty(value = "缴费凭证", example = "缴费图片")
    private byte[] paymentImage;

    @ExcelProperty("身份证证件")
    @ApiModelProperty(value = "身份证证件", example = "身份证图片")
    private byte[] idCardImage;

}
