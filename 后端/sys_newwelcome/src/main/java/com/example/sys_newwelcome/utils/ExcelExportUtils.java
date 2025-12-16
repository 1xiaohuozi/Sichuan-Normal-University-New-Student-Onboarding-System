package com.it2.springbootweb2.util;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;

/**
 * Excel导出
 *
 * @author 罗航
 */
public class ExcelExportUtils {

    /**
     * 导出Excel
     *
     * @param headList  Excel表头列表
     * @param dataList  Excel内容列表
     * @param sheetName Excel工作表名称
     * @param fileName 文件名
     * @param response  响应对象
     * @throws IOException
     */
    public static void exportExcel(List<String> headList, List<LinkedHashMap<String, Object>> dataList, String sheetName, String fileName, HttpServletResponse response)
            throws IOException {
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            if (null == fileName|| fileName.trim().equals("")) { //默认文件名
                fileName = new String((sheetName + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), StandardCharsets.UTF_8);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            ExcelWriter writer = new ExcelWriter(servletOutputStream, ExcelTypeEnum.XLSX, true);
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName(sheetName);

            Table table = new Table(1);
            List<List<String>> head = new ArrayList<>();
            if (!CollectionUtils.isEmpty(headList)) {
                headList.forEach(headName -> head.add(Arrays.asList(headName)));
            }
            table.setHead(head);

            List<List<String>> data = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataList)) {
                dataList.forEach(map -> {
                    List<String> list = new ArrayList<>();
//                    for (Entry<String, Object> entry : map.entrySet()) {
//                        Object value = entry.getValue();
//                        list.add(value == null ? null : value.toString());
//                    }
                    for (String title:headList) {
                        Object value = map.get(title);
//                        Object value = entry.getValue();
                        list.add(value == null ? null : value.toString());
                    }
                    data.add(list);
                });
            }

            writer.write0(data, sheet, table);
            writer.finish();
            servletOutputStream.flush();
        } catch (IOException e) {
            throw new IOException(e.toString());
        } finally {
            if (servletOutputStream != null) {
                servletOutputStream.close();
            }
        }
    }
}

