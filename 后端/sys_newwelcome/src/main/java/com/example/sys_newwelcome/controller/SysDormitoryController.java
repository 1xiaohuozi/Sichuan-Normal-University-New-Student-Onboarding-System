package com.example.sys_newwelcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sys_newwelcome.common.dto.SysDormitoryDto;
import com.example.sys_newwelcome.common.lang.Result;
import com.example.sys_newwelcome.entity.SysDormitory;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysDormitoryMapper;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysDormitoryService;
import com.example.sys_newwelcome.service.SysUserService;
import com.example.sys_newwelcome.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Api(value = "寝室分配管理", tags = "寝室分配管理接口")
@RequestMapping("/sys/dormitory")
public class SysDormitoryController extends BaseController{

    @Autowired
    SysDormitoryMapper sysDormitoryMapper;
    @Autowired
    SysDormitoryService sysDormitoryService;
    @Autowired
    private RedisTemplate<String,Long> redisTemplate;
    @Autowired
    private RedisTemplate<String,String> redisTemplate2;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PageUtils pageUtil;

    /**
     * 获取宿舍选取信息列表
     */
    @ApiOperation(value = "获取宿舍选取信息列表", notes = "宿舍选取列表")
    @GetMapping("/list")
    public Result getDormitoryList(){
        List<SysDormitory> sysDormitoryList = sysDormitoryMapper.selectList(null);
        List<SysDormitoryDto> sysDormitoryDtoList = sysDormitoryList.stream()
                .map(sysDormitory -> {
                    SysDormitoryDto sysDormitoryDto = new SysDormitoryDto();
                    /**
                     * 将 SysDormitory 中的属性复制到 SysDormitoryDto
                     */
                    BeanUtils.copyProperties(sysDormitory, sysDormitoryDto);
                    QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("id",sysDormitory.getUserId());
                    SysUser sysUser = sysUserMapper.selectOne(queryWrapper1);
                    if(sysDormitoryDto != null){
                        sysDormitoryDto.setName(sysUser.getName());
                        sysDormitoryDto.setUsername(sysUser.getUsername());
                        sysDormitoryDto.setInstitute(sysUser.getInstitute());
                        sysDormitoryDto.setSpeciality(sysUser.getSpeciality());
                        sysDormitoryDto.setClassNumber(sysUser.getClassNumber());
                    }
                    return sysDormitoryDto;
                })
                .collect(Collectors.toList());
        Comparator<SysDormitoryDto> comparator = Comparator
                .comparing(SysDormitoryDto::getInstitute, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysDormitoryDto::getSpeciality, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysDormitoryDto::getClassNumber, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysDormitoryDto::getUsername, Comparator.nullsLast(Comparator.naturalOrder()));


        sysDormitoryDtoList.sort(comparator);
        Page<SysDormitoryDto> sysDormitoryDtoPage = pageUtil.pageList(sysDormitoryDtoList,req);
        return Result.success(sysDormitoryDtoPage);
    }



    /**
     * 学院筛寝室分配信息
     */
    @ApiOperation(value = "筛选学院信息", notes = "学院学生筛选")
//    @PreAuthorize("hasAnyAuthority('sys:dormitory:branch:institute')")
    @PostMapping("/branch/institute")
    public Result branchInstitute(@RequestBody String institute){
        List<SysUser> sysUserList = sysUserService.getDormitoryListByInstitute(institute.toString());
        List<SysDormitoryDto> sysDormitoryDtoList = sysUserList.stream()
                .map(sysUser -> {
                    SysDormitoryDto sysDormitoryDto = new SysDormitoryDto();
                    /**
                     * 将 SysDormitory 中的属性复制到 SysDormitoryDto
                     */
                    BeanUtils.copyProperties(sysUser, sysDormitoryDto);
                    QueryWrapper<SysDormitory> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("user_id",sysUser.getId());
                    SysDormitory sysDormitory = sysDormitoryMapper.selectOne(queryWrapper1);
                    if(sysDormitory != null){
                        sysDormitoryDto.setCampus(sysDormitory.getCampus());
                        sysDormitoryDto.setRegion(sysDormitory.getRegion());
                        sysDormitoryDto.setBedNumber(sysDormitory.getBedNumber());
                    }
                    return sysDormitoryDto;
                })
                .collect(Collectors.toList());
        Comparator<SysDormitoryDto> comparator = Comparator
                .comparing(SysDormitoryDto::getSpeciality, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysDormitoryDto::getClassNumber, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysDormitoryDto::getUsername, Comparator.nullsLast(Comparator.naturalOrder()));
        sysDormitoryDtoList.sort(comparator);

        Page<SysDormitoryDto> sysDormitoryDtoPage = pageUtil.pageList(sysDormitoryDtoList,req);
        return Result.success(sysDormitoryDtoPage);
    }


    /**
     * 更新用户宿舍选取信息
     */
    @ApiOperation(value = "更新宿舍选取信息", notes = "更新用户的宿舍选取信息")
//    @PreAuthorize("hasAnyAuthority('sys:dormitory:update')")
    @PostMapping("/update")
//    @HoneyLogs(operation = "宿舍", type = "修改",url = "sys:dormitory:update")
    public Result update(@Validated @RequestBody SysDormitory sysDormitory){
        SysDormitory sysDormitory1 = sysDormitoryService.getById(sysDormitory.getId());
        sysDormitoryService.updateById(sysDormitory);
        List<String> stringList = new ArrayList<>();
        stringList.add(sysDormitory1.toString());
        stringList.add(sysDormitory.toString());
        return Result.success(stringList);
    }

    /**
     * 删除信息
     */
    @ApiOperation(value = "删除宿舍选取信息", notes = "删除用户的宿舍选取信息")
//    @PreAuthorize("hasAnyAuthority('sys:dormitory:delete')")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long id) {
        boolean flagLog = sysDormitoryService.removeById(id);
        return Result.success(flagLog);
    }

    /**
     * 根据userId获取宿舍选取信息
     */
    @ApiOperation(value = "获取宿舍选取信息", notes = "根据userId获取宿舍选取信息")
    @PostMapping("/info")
    public Result getDormitory(@RequestBody Long id){
        /**
         * 查询userId对应宿舍选取信息
         */
        QueryWrapper<SysDormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);

        SysDormitory sysDormitory = sysDormitoryMapper.selectOne(queryWrapper);
        return Result.success(sysDormitory);
    }

    /**
     * 创建保存或更新的选床信息
     */
    @ApiOperation(value = "创建或更新选床信息", notes = "创建或更新选床信息")
//    @PreAuthorize("hasAnyAuthority('sys:dormitory:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysDormitory sysDormitory) {

        /**
         * 声明一个uuid，将作为一个value放入我们的key所对应的值中
         */
        String uuid = UUID.randomUUID().toString();
        /**
         * 定义一个锁：luo脚本可以使用同一把锁，来实现
         */
        String dormitory = getCacheKey(sysDormitory);
        String lockKey = "lock:" + dormitory;

        /**
         * 获取锁
         */
        Boolean lock = redisTemplate2.opsForValue().setIfAbsent(lockKey, uuid, 3, TimeUnit.SECONDS);

        if (lock) {
            /**
             * 使用luo脚本来锁
             */
            /**
             * 定义luo脚本
             */
            String script = "if redis.call('get',KEYS[1]) == ARGV[1] " +
                    "then return redis.call('del',KEYS[1]); else return 0 end";
            /**
             * 使用redis执行luo
             */
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);


            String cacheKey = getCacheKey(sysDormitory);
            /**
             * 先尝试从缓存中获取数据
             */
            Object cacheData = redisTemplate.opsForValue().get(cacheKey);

            if (cacheData == null) {
                // 查询是否存在相同的床位信息
                QueryWrapper<SysDormitory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("campus", sysDormitory.getCampus())
                        .eq("region", sysDormitory.getRegion())
                        .eq("dormitory", sysDormitory.getDormitory())
                        .eq("dormitory_number", sysDormitory.getDormitoryNumber())
                        .eq("bed_number", sysDormitory.getBedNumber())
                        .ne("user_id", sysDormitory.getUserId());

                int count = sysDormitoryMapper.selectCount(queryWrapper);

                // 如果存在相同的床位信息，返回错误信息
                if (count > 0) {
                    return Result.fail("床位已经被选择");
                }
                QueryWrapper<SysDormitory> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("user_id", sysDormitory.getUserId());
                int count2 = sysDormitoryMapper.selectCount(queryWrapper1);
                // 存选床信息
                if (count2 > 0) {
                    SysDormitory sysDormitory1 = sysDormitoryMapper.selectOne(queryWrapper1);
                    String dlCacheKey = getCacheKey(sysDormitory1);
                    redisTemplate.delete(dlCacheKey);
                    sysDormitory.setUpdated(LocalDateTime.now());
                    sysDormitoryService.update(sysDormitory, queryWrapper1);
                } else {
                    sysDormitory.setCreated(LocalDateTime.now());
                    sysDormitoryService.save(sysDormitory);
                }
                /**
                 * 将数据放放入缓存并设置过期时间
                 */
                redisTemplate.opsForValue().set(cacheKey, sysDormitory.getUserId(), 3, TimeUnit.HOURS);
                redisTemplate2.execute(redisScript, Arrays.asList(lockKey), uuid);
                return Result.success(sysDormitory);
            } else {
                if (cacheData.toString().equals(sysDormitory.getUserId().toString())) {
                    sysDormitory.setUpdated(LocalDateTime.now());
                    QueryWrapper<SysDormitory> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.eq("user_id", sysDormitory.getUserId());
                    sysDormitoryService.update(sysDormitory, queryWrapper2);
                    redisTemplate.opsForValue().set(cacheKey, sysDormitory.getUserId(), 3, TimeUnit.HOURS);
                    redisTemplate2.execute(redisScript, Arrays.asList(lockKey), uuid);
                    return Result.success(sysDormitory);
                } else {
                    redisTemplate2.execute(redisScript, Arrays.asList(lockKey), uuid);
                    return Result.fail("床位已经被选择");
                }
            }
        } else {
            /**
             * 最大尝试数
             */
            int maxAttempts = 3;
            int currentAttempt = 0;

            while (currentAttempt < maxAttempts) {
                /**
                 * 尝试获取锁
                 */
                Boolean lock1 = redisTemplate2.opsForValue().setIfAbsent(lockKey, uuid, 3, TimeUnit.SECONDS);
                if(lock1){
                    redisTemplate2.delete(lockKey);
                    save(sysDormitory);
                }else{
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt(); // 重新设置中断状态
                    }
                    currentAttempt++;
                }
            }
            /**
             * 如果尝试多次仍未获取到锁，返回失败
             */
            return Result.fail("无法获取锁，有人正在选该宿舍");
        }
    }

    private  String getCacheKey(SysDormitory sysDormitory){
        return "dormitory:" +
                sysDormitory.getCampus() + ":" +
                sysDormitory.getRegion() + ":" +
                sysDormitory.getDormitory() + ":" +
                sysDormitory.getDormitoryNumber() + ":" +
                sysDormitory.getBedNumber();
    }
}

