package com.timeworx.modules.security.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/3 11:31 PM
 */
public class Oauth2Token implements AuthenticationToken {
    private String token;

    public Oauth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
