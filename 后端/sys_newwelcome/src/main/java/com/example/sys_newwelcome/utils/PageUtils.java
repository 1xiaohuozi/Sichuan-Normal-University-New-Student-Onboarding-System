package com.example.sys_newwelcome.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.controller.BaseController;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class PageUtils extends BaseController{

    public <T> Page<T> pageList(List<T> dataList,HttpServletRequest req) {

        int current = ServletRequestUtils.getIntParameter(req, "current", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 40);

        int startIndex = (current - 1) * size;
        int endIndex = Math.min(startIndex + size, dataList.size());

        Page<T> page = new Page<>(current, size);
        page.setRecords(dataList.subList(startIndex, endIndex));
        page.setTotal(dataList.size());

        return page;
    }
}
