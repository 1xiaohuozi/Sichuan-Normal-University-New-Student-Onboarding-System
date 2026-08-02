package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@TableName("sys_channel")
@ApiModel(value = "SysChannel", description = "绿色通道实体类")
public class SysChannel extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ExcelProperty("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ExcelProperty("申请类型")
    @ApiModelProperty(value = "申请类型", example = "低保户")
    private String povertyType;

    @ExcelProperty("申请理由")
    @ApiModelProperty(value = "申请理由", example = "因为我......")
    private String reason;

    @ExcelProperty("申请凭证")
    @ApiModelProperty(value = "申请凭证", example = "图片")
    private byte[] povertyProof;

    @ExcelProperty("申请金额")
    @ApiModelProperty(value = "申请金额", example = "2100")
    private Integer feeAmount;
}
