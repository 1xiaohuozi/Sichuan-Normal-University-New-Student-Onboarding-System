package com.example.sys_newwelcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys_newwelcome.entity.SysPayment;
import com.example.sys_newwelcome.mapper.SysPaymentMapper;
import com.example.sys_newwelcome.service.SysPaymentService;
import org.springframework.stereotype.Service;

@Service
public class SysPaymentServiceImpl extends ServiceImpl<SysPaymentMapper, SysPayment> implements SysPaymentService {
}
