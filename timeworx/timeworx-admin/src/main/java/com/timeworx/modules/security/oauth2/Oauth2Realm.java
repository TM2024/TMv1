package com.timeworx.modules.security.oauth2;


import com.timeworx.common.entity.user.User;
import com.timeworx.modules.security.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Description 认证
 * @Author: ryzhang
 * @Date 2023/2/3 10:30 PM
 */
@Component
public class Oauth2Realm extends AuthorizingRealm {

    @Resource
    private ShiroService shiroService;

    /**
     * realm必须支持接受token
     * @param token the token being submitted for authentication.
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Oauth2Token;
    }

    /**
     * 授权(验证权限时调用)
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getName();

        //用户权限列表
        Set<String> permissionSet = shiroService.getUserPermissions(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissionSet);

        return simpleAuthorizationInfo;
    }

    /**
     * 认证(登录时调用)
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String accessToken = (String) token.getPrincipal();

        // 根据accessToken，查询用户信息
        String userId = shiroService.getUserByToken(accessToken);
        // token失效
        if(StringUtils.isBlank(userId)){
            throw new IncorrectCredentialsException();
        }

        // 查询用户信息
        User user = shiroService.findUserById(Long.parseLong(userId));

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
