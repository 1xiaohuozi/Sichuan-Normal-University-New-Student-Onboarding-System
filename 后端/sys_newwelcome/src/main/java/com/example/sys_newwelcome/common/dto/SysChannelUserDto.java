package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysChannelUserDto extends SysPaymentUserDto{
    @ExcelProperty("申请类型")
    @ApiModelProperty(value = "申请类型", example = "低保户")
    private String povertyType;

    @ExcelProperty("申请理由")
    @ApiModelProperty(value = "申请理由", example = "因为我......")
    private String reason;

    @ExcelProperty("申请金额")
    @ApiModelProperty(value = "申请金额", example = "2100")
    private Integer feeAmount;
}
