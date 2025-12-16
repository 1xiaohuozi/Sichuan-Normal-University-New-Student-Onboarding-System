package com.example.sys_newwelcome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sys_newwelcome.common.email.constant.HttpStatusEnum;
import com.example.sys_newwelcome.common.email.constant.RedisConstant;
import com.example.sys_newwelcome.common.email.param.LoginParam;
import com.example.sys_newwelcome.common.email.vo.R;
import com.example.sys_newwelcome.common.lang.Const;
import com.example.sys_newwelcome.entity.SysMenu;
import com.example.sys_newwelcome.entity.SysRole;
import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.mapper.SysUserMapper;
import com.example.sys_newwelcome.service.SysMenuService;
import com.example.sys_newwelcome.service.SysRoleService;
import com.example.sys_newwelcome.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys_newwelcome.utils.RedisUtils;
import com.example.sys_newwelcome.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.service.impl.SysUserServiceImpl
 * @version:1.0
 */
/**
 * 使用 SysUserMapper 进行数据库访问，而 SysUser 是与数据库表对应的实体对象
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    @Lazy
    private SysMenuService sysMenuService;
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public R findPassword(LoginParam loginParam) {
        if (loginParam == null) {
            return R.error(HttpStatusEnum.PARAM_ILLEGAL);
        }

        // 获取参数
        String email = loginParam.getEmail();
        String password = loginParam.getPassword1();
        String code = loginParam.getCode();

        if (StringUtils.isAnyBlank(email, password, code)) {
            // 非空
            return R.error(HttpStatusEnum.PARAM_ILLEGAL);
        }else if (!StringUtil.checkEmail(email)) {
            // 邮箱格式校验
            return R.error(HttpStatusEnum.EMAIL_ERROR);
        }else if (!StringUtil.checkPassword(password) || code.length() != 5) {
            // 密码格式和验证码长度校验
            return R.error(HttpStatusEnum.PARAM_ILLEGAL);
        }

        // 构造查询条件对象
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("email", email);
        wrapper.last("limit 1");

        // 查询用户，是否存在
        SysUser user = this.baseMapper.selectOne(wrapper);
        if (user == null) {
            return R.error(HttpStatusEnum.USER_NOT_EXIST);
        }

        // 获取正确的验证码
        String rightCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL + email);
        if (!code.equals(rightCode)) {
            // 验证码比对
            return R.error(HttpStatusEnum.CODE_ERROR);
        }

        // 删除验证码
        redisTemplate.delete(RedisConstant.EMAIL + email);

        // 修改密码
        SysUser user1 = new SysUser();
        user1.setId(user.getId());
        user1.setPassword(passwordEncoder.encode(password));

        // 修改
        return this.baseMapper.updateById(user1) == 0 ? R.error(HttpStatusEnum.UNKNOWN_ERROR) : R.ok();
    }


    /**
     * 根据用户名检索用户信息
     */
    @Override
    public SysUser getByUserName(String username){
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    /**
     * 根据userId查找用户权限数据
     */
    @Override
    public String getUserAuthorityInfo(Long userId){
        SysUser sysUser =sysUserMapper.selectById(userId);
        String authority = null;
        /**
         * 如果redis缓存中已经存在以用户名为键的数据，则直接从缓存中获取
         * 否则查询并缓存
         */
        if(redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
        }else{
            /**
             * 查询userId对应的roleId及其权限信息
             */
            List<SysRole> roles = sysUserService.list(new QueryWrapper<SysRole>().inSql("id","select role_id from sys_user_role where user_id = " + userId));
            /**
             * 若存在用户角色
             */
            if(roles.size() > 0){
                /**
                 * 流是一种抽象数据类型，允许对数据集合进行一系列的操作（过滤、映射、归约等）
                 * 将roles列表转换为一个stream流
                 * 将每个r与“ROLE_”凭借起来
                 * 将流中的元素收集起来并以逗号分隔的形式连接成一个字符串
                 */
                String roleCode = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCode.concat(",");
            }
            /**
             * 根据userId查询到了对应的角色所拥有的权限菜单
             */
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            /**
             * 若找到了菜单menuId
             */
            if(menuIds.size() > 0) {
                /**
                 * 查找对应menuId或许对应菜单的详细信息
                 */
                List<SysMenu> sysMenus = sysMenuService.listByIds(menuIds);
                /**
                 * 提取每个SysMenu对象的perms属性（菜单权限）值并用逗号分隔成字符串
                 */
                String menusPerms = sysMenus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                /**
                 * 将menusPerms权限字符串追加到authority字符串
                 */
                authority = authority.concat(menusPerms);
            }
            /**
             * 缓存为3600秒
             */
            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(),authority,60*60);
        }
        /**
         * 得到角色代码和权限字符串的组合
         */
        return authority;
    }

    /**
     * 删除指定的键
     */
    @Override
    public void clearUserAuthorityInfo(String username){
        redisUtil.del("GrantedAuthority:" + username);
    }

    /**
     *  根据角色Id清楚用户权限信息缓存
     */
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId){
        /**
         * 根据role_Id查询用户信息
         */
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>().inSql("id","select user_id from sys_user_role where role_id = " + roleId));
        /**
         * 循环遍历查找到的用户列表，依次清除用户的权限信息缓存
         */
        sysUsers.forEach(u ->{
          this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    /**
     * 根据menuId清除用户权限信息缓存
     */
    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId){
        /**
         * 根据menuId查询用户信息
         */
        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);
        /**
         * 循环遍历用户清除权限信息缓存
         */
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public int countReportByInstitute(String institute) {
        return sysUserMapper.getByUserIds(institute);
    }

    @Override
    public int countReportByClassNumber(String institute,String classNumber) {
        return sysUserMapper.getByUserIds2(institute,classNumber);
    }


    @Override
    public int countPassPaymentByInstitute(String institute){
        return sysUserMapper.getByPassPaymentInstitute(institute);
    }

    @Override
    public int countReviewPaymentByInstitute(String institute){
        return sysUserMapper.getByReviewPaymentInstitute(institute);
    }


    @Override
    public int countPassPaymentByClassNumber(String institute,String classNumber){
        return sysUserMapper.getByPassPaymentClassNumber(institute,classNumber);
    }

    @Override
    public int countReviewPaymentByClassNumber(String institute,String classNumber){
        return sysUserMapper.getByReviewPaymentClassNumber(institute,classNumber);
    }


    @Override
    public int countPassChannelByInstitute(String institute){
        return sysUserMapper.getByPassChannelInstitute(institute);
    }

    @Override
    public int countReviewChannelByInstitute(String institute){
        return sysUserMapper.getByReviewChannelInstitute(institute);
    }


    @Override
    public int countPassChannelByClassNumber(String institute,String classNumber){
        return sysUserMapper.getByPassChannelClassNumber(institute,classNumber);
    }

    @Override
    public int countReviewChannelByClassNumber(String institute,String classNumber){
        return sysUserMapper.getByReviewChannelClassNumber(institute,classNumber);
    }

    @Override
    public List<SysUser> getUserListByReport(){
        return sysUserMapper.getUserByReport();
    }

    @Override
    public List<SysUser> getUserListByReportInstitute(String institute){
        return sysUserMapper.getUserByReportInstitute(institute);
    }

    @Override
    public List<SysUser> getUserListByReportClassNumber(String institute,String classNumber){
        return sysUserMapper.getUserByReportClassNumber(institute,classNumber);
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByPayment(int statu){
        return sysUserMapper.getPassOrReviewUserByPayment(statu);
    }

    @Override
    public List<SysUser> getNotUserListByPayment(){
        return sysUserMapper.getNotUserByPayment();
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByPaymentInstitute(String institute,int statu){
        return sysUserMapper.getPassOrReviewUserByPaymentInstitute(institute,statu);
    }

    @Override
    public List<SysUser> getNotUserListByPaymentInstitute(String institute){
        return sysUserMapper.getNotUserByPaymentInstitute(institute);
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByPaymentClassNumber(String institute,String classNumber,int statu){
        return sysUserMapper.getPassOrReviewUserByPaymentClassNumber(institute,classNumber,statu);
    }

    @Override
    public List<SysUser> getNotUserListByPaymentClassNumber(String institute,String classNumber){
        return sysUserMapper.getNotUserByPaymentClassNumber(institute,classNumber);
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByChannel(int statu){
        return sysUserMapper.getPassOrReviewUserByChannel(statu);
    }

    @Override
    public List<SysUser> getNotUserListByChannel(){
        return sysUserMapper.getNotUserByChannel();
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByChannelInstitute(String institute,int statu){
        return sysUserMapper.getPassOrReviewUserByChannelInstitute(institute,statu);
    }

    @Override
    public List<SysUser> getNotUserListByChannelInstitute(String institute){
        return sysUserMapper.getNotUserByChannelInstitute(institute);
    }

    @Override
    public List<SysUser> getPassOrReviewUserListByChannelClassNumber(String institute,String classNumber,int statu){
        return sysUserMapper.getPassOrReviewUserByChannelClassNumber(institute,classNumber,statu);
    }

    @Override
    public List<SysUser> getNotUserListByChannelClassNumber(String institute,String classNumber){
        return sysUserMapper.getNotUserByChannelClassNumber(institute,classNumber);
    }

    @Override
    public List<SysUser> getDormitoryListByInstitute(String institute){
        return sysUserMapper.getDormitoryByInstitute(institute);
    }

    @Override
    public List<SysUser> getNotUserListByReport(){
        return sysUserMapper.getNotUserByReport();
    }

    @Override
    public List<SysUser> getNotUserListByReportInstitute(String institute) {
        return sysUserMapper.getNotUserByReportInstitute(institute);
    }

    @Override
    public List<SysUser> getNotUserListByReportClassNumber(String institute, String classNumber) {
        return sysUserMapper.getNotUserByReportClassNumber(institute,classNumber);
    }


}
