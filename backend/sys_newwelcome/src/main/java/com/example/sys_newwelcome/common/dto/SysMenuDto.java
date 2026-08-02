package com.example.sys_newwelcome.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuDto implements Serializable {
    @ExcelProperty("id")
    private Long id;
    @ExcelProperty("菜单名")
    private String name;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("图标")
    private String icon;
    @ExcelProperty("url")
    private String path;
    @ExcelProperty("备注")
    private String component;
    @ExcelProperty("返回列表")
    private List<SysMenuDto> children = new ArrayList<>();
}
