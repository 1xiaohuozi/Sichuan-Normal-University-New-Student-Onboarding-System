package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysICNStatu extends SysInstituteClassNumber{
    @ExcelProperty("状态")
    @ApiModelProperty(value = "状态", example = "1")
    private Integer statu;
}
