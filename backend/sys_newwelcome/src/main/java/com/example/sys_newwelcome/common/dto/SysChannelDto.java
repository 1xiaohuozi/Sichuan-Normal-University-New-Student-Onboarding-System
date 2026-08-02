package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.sys_newwelcome.entity.SysChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.multiset.SynchronizedMultiSet;

import javax.validation.constraints.NotBlank;

@Data
public class SysChannelDto extends SysChannel{
    @NotBlank(message = "用户名不能为空")
    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名", example = "某某某")
    private String username;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "姓名", example = "John Doe")
    private String name;
}
