package com.example.sys_newwelcome.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.entity.SysUser
 * @version:1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@ApiModel(value = "SysUser", description = "用户实体类")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @ExcelProperty("用户名")
    @ApiModelProperty(value = "用户名", example = "john_doe")
    @Size(min=2,max=10,message = "用户名需要在2~10个字符内")
    private String username;

    @ExcelProperty("密码")
    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "姓名", example = "John Doe")
    private String name;

    @ExcelProperty("性别")
    @ApiModelProperty(value = "性别", example = "男")
    private String sex;

    @ExcelProperty("头像")
    @ApiModelProperty(value = "头像", example = "avatar.png")
    private String avatar;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ExcelProperty("邮箱")
    @ApiModelProperty(value = "邮箱", example = "john@example.com")
    private String email;

    @ExcelProperty("城市")
    @ApiModelProperty(value = "城市", example = "New York")
    private String city;

    @ExcelProperty("电话")
    @ApiModelProperty(value = "电话", example = "11234567890")
    private String phone;

    @ExcelProperty("最后登录时间")
    @ApiModelProperty(value = "最后登录时间", example = "2023-10-30 12:00:00")
    private LocalDateTime lastLogin;

    @ExcelProperty("学院")
    @ApiModelProperty(value = "学院", example = "计算机科学学院")
    private String institute;

    @ExcelProperty("专业")
    @ApiModelProperty(value = "专业", example = "软件工程")
    private String speciality;

    @ExcelProperty("班级")
    @ApiModelProperty(value = "班级", example = "2021级3班")
    private String classNumber;

    @ExcelProperty("寝室楼")
    @ApiModelProperty(value = "寝室楼", example = "西苑四栋")
    private String dormitory;

    @ExcelProperty("寝室号")
    @ApiModelProperty(value = "寝室号", example = "319")
    private String dormitoryNumber;

    @ExcelProperty("民族")
    @ApiModelProperty(value = "民族", example = "汉族")
    private String nation;

    @ExcelProperty("身份证")
    @ApiModelProperty(value = "身份证", example = "510121XXXXXXXXXXX")
    private String idCard;

    @ExcelProperty("学制")
    @ApiModelProperty(value = "学制", example = "4年")
    private String academic;

    @ExcelProperty("学历")
    @ApiModelProperty(value = "学历", example = "本科")
    private String education;

    @ExcelProperty("出生日期")
    @ApiModelProperty(value = "出生日期", example = "2003年3月23日")
    private String birth;

    @ExcelProperty("政治面貌")
    @ApiModelProperty(value = "政治面貌", example = "共青团员")
    private String politic;

    @ExcelProperty("状态情况")
    @ApiModelProperty(value = "状态情况", example = "1")
    private Integer state;

    @ExcelProperty("职务")
    @ApiModelProperty(value = "职务", example = "学生")
    private String duties;

    @TableField(exist = false)
    private List<SysRole> sysRoles = new ArrayList<>();

//    @TableField(exist = false)
//    private SysRole[] sysRole;

}
