package com.example.sys_newwelcome.security;

import cn.hutool.core.lang.Assert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;

/**
 * @Author:罗航
 * @Date：2023
 * @Description:com.example.sys_newwelcome.security.AccountUser
 * @version:1.0
 */

/**
 * 表示用户详细信息
 */
public class AccountUser implements UserDetails {
    private long userId;
    private String password;
    private final String username;
    /**
     *  Collection指定集合 包含GrantedAuthority类型或继承类型
     *  集合表示用户的角色权限
     */
    private final Collection<? extends GrantedAuthority> authorities;
    /**
     * 用户的账户是否未过期
     */
    private final boolean accountNonExpired;
    /**
     * 用户的账户是否未锁定
     */
    private final boolean accountNonLocked;
    /**
     * 用户的凭证是否未过期
     */
    private final boolean credentialsNonExpired;
    /**
     * 用户的账户是否已启用
     */
    private final boolean enabled;

    /**
     * @param userId      = userId
     * @param username    = username
     * @param password    = password
     * @param authorities = authorities
     *                    enabled、accountNonExpired、credentialsNonExpired、accountNonLocked默认值为true
     */
    public AccountUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities){
        this(userId,username,password,true,true,true,true,authorities);
    }

    /**
     * 指定参数
     * @param userId
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public AccountUser(Long userId,String username,String password,boolean enabled,boolean accountNonExpired,boolean credentialsNonExpired,boolean accountNonLocked,Collection<? extends  GrantedAuthority> authorities){
        Assert.isTrue(username != null && !"".equals(username) && password != null,"Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.authorities;
    }
    @Override
    public String getPassword(){
        return this.password;
    }
    @Override
    public String getUsername(){
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired(){
        return this.accountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked(){
        return this.accountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return this.credentialsNonExpired;
    }
    @Override
    public boolean isEnabled(){
        return this.enabled;
    }


}
