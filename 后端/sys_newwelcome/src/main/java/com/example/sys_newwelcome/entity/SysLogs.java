package com.example.sys_newwelcome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysLogs {
    @TableId(type = IdType.AUTO)
    /**
     * 自增序号
     */
    private Integer id;
    /**
     * 操作模块
     */
    private String operation;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 操作时间
     */
    private String time;
    /**
     * 操作内容
     */
    private String info;
    /**
     * 接口
     */
    private String url;
}