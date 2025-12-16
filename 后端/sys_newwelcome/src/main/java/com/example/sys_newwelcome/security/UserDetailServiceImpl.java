package com.example.sys_newwelcome.security;

import com.example.sys_newwelcome.entity.SysUser;
import com.example.sys_newwelcome.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.UserDetailServiceImpl
 * @version:1.0
 */

/**
 * 从数据库加载用户详细信息，进行身份验证和授权
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Lazy
    @Autowired
    private SysUserService sysUserService;

    /**y
     * 根据用户名加载用户的详细信息，当用户尝试进行身份验证的时被调用
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUserName(username);
        /**
         * 未匹配到用户
         */
        if(ObjectUtils.isEmpty(sysUser)){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        /**
         * 匹配到用户返回AccountUser详细信息
         */
        return new AccountUser(sysUser.getId(),sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));

    }

    /**
     * 角色权限获取
     */
    public List<GrantedAuthority> getUserAuthority(Long userId){
        /**
         * 通过userId获取权限信息
         */
        String authority = sysUserService.getUserAuthorityInfo(userId);
        /**
         * 逗号风格的字符串authority转换为GrantedAuthority对象的列表
         */
        System.out.println(authority);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
