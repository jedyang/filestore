package com.yunsheng.filestore.config;


import com.yunsheng.filestore.entity.User;
import com.yunsheng.filestore.service.RoleService;
import com.yunsheng.filestore.service.UserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * Created by yys
 */
//@Component("myShiroRealm")
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 授权信息
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录时输入的用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        if (username != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            Set<String> roles = roleService.getRoles(username);
            info.setRoles(roles);
             // 权限

            info.setStringPermissions(null);
            return info;
        }
        return null;
    }

    /**
     * 登录认证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();

        User userByUsername = userService.getUserByUsername(username);
        if (null == userByUsername) {
            return null;
        }

        // 返回一个正确的用户认证信息即可
        return new SimpleAuthenticationInfo(userByUsername.getUsername(), userByUsername.getPassword(), "getName()");

    }
}
