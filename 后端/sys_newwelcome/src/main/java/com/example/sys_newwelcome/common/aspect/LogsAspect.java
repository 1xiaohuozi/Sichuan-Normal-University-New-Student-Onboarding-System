package com.example.sys_newwelcome.common.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sys_newwelcome.common.annotation.HoneyLogs;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysLogs;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysLogsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Component
@Aspect
@Slf4j
public class LogsAspect {
    private static String ip;

    public static void setIp(String Ip){
        ip = Ip;
    }
    @Resource
    SysLogsService sysLogsService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisTemplate<String,SysLogs> redisTemplate;

    @AfterReturning(pointcut = "@annotation(honeyLogs)", returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, HoneyLogs honeyLogs, Result jsonResult) {
        try{
            // 获取当前登录的用户的信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();

            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",name);
            SysUser loginUser = sysUserMapper.selectOne(queryWrapper);
            System.out.println(loginUser);
            if (loginUser == null) { // 用户未登录的情况下  loginUser是null  是null的话我们就要从参数里面获取操作人信息
                // 登录、注册
                Object[] args = joinPoint.getArgs();
                if (ArrayUtil.isNotEmpty(args)) {
                    if (args[0] instanceof SysUser) {
                        loginUser = (SysUser) args[0];
                    }
                }
            }
            if (loginUser == null) {
                log.error("记录日志信息报错，未获取到当前操作用户信息");
                return;
            }
            SysLogs sysLogs = new SysLogs();
            sysLogs.setInfo(jsonResult.getData().toString());
            sysLogs.setIp(ip);
            sysLogs.setOperation(honeyLogs.operation());
            sysLogs.setType(honeyLogs.type());
            sysLogs.setUrl(honeyLogs.url());
            sysLogs.setTime(DateUtil.now());
            sysLogs.setUsername(name);
            System.out.println(sysLogs);
            cacheLogs(sysLogs, name);
        }catch (Exception e) {
            log.error("记录日志信息报错", e);
        }
    }
    //使用Redis缓存日志信息到名为globalSysLogsList:用户名的列表中。
    private void cacheLogs(SysLogs sysLogs, String userName) {
        ListOperations<String, SysLogs> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("globalSysLogsList:" + userName, sysLogs);
        System.out.println(redisTemplate.getExpire("globalSysLogsList:"));
    }
//缓存存储数据库
    @Scheduled(fixedRate = 3600000)
    public void saveLogsFromRedisToDatabase() {
        try {
            Set<String> userKeys = redisTemplate.keys("globalSysLogsList:*");

            if (userKeys != null) {
                for (String userKey : userKeys) {
                    ListOperations<String, SysLogs> listOperations = redisTemplate.opsForList();
                    List<SysLogs> sysLogsList = listOperations.range(userKey, 0, -1);

                    if (sysLogsList != null && !sysLogsList.isEmpty()) {
                        sysLogsService.saveBatch(sysLogsList);
                        listOperations.trim(userKey, 1, 0);//将用户的日志列表批量保存到数据库，并清除Redis中已保存到数据库的日志
                    }
                }
            }
        } catch (Exception e) {
            log.error("定时保存日志到数据库出错", e);
        }
    }
}