package com.example.sys_newwelcome.common.lang;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.common.lang.Result
 * @version:1.0
 */
@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        return success(200,"操作成功",data);
    }

    public static Result success(int code,String msg,Object data){
        Result r = new Result();
        r.setData(data);
        r.setMsg(msg);
        r.setCode(code);
        return r;
    }
    public static Result fail(String msg){
        return fail(400,msg, null);
    }

    public static Result fail(int code,String msg,Object data){
        Result r = new Result();
        r.setData(data);
        r.setMsg(msg);
        r.setCode(code);
        return r;
    }

}

