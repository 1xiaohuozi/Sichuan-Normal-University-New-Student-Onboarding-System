package com.example.sys_newwelcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys_newwelcome.entity.SysLogs;
import com.example.sys_newwelcome.mapper.SysLogsMapper;
import com.example.sys_newwelcome.service.SysLogsService;
import org.springframework.stereotype.Service;

@Service
public class SysLogsServiceImpl extends ServiceImpl<SysLogsMapper, SysLogs> implements SysLogsService {
}
