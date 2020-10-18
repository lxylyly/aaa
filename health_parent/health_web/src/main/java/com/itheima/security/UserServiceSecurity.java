package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//这个类相当于一个Controller类,开启了注解包扫描,需要加注解,否则就要到核心配置文件中注册
@Component
public class UserServiceSecurity implements UserDetailsService {
    //通过dubbo远程调用服务提供者
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//根据用户名去数据库查询出这个对象,包含账号密码和权限和角色(调用service进行查询)
        //需要根据三个表查到所有信息 角色,角色关键字,权限,权限关键字
        com.itheima.pojo.User user=userService.findByUserName(username);
        System.out.println(".......");
        if (user==null){
            return null;
        }
        List<GrantedAuthority>list=new ArrayList();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
               list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //这里new一个User传入这三个参数返回
        UserDetails userDetails = new User(username, user.getPassword(), list);
        return userDetails;
    }
}
