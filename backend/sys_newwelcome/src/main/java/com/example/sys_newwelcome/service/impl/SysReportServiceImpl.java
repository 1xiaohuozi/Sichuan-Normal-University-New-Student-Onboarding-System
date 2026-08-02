package com.example.sys_newwelcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys_newwelcome.entity.SysReport;
import com.example.sys_newwelcome.mapper.SysReportMapper;
import com.example.sys_newwelcome.service.SysReportService;
import org.springframework.stereotype.Service;

@Service
public class SysReportServiceImpl extends ServiceImpl<SysReportMapper, SysReport> implements SysReportService{
}
