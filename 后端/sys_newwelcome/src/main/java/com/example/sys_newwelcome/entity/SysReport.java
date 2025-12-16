package com.example.sys_newwelcome.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@TableName("sys_report")
@ApiModel(value = "SysReport", description = "报道信息实体类类")
public class SysReport extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    @ExcelProperty("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

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

    @TableField(exist = false)
    private List<SysReport> reports = new ArrayList<>();
}
