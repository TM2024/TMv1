package com.timeworx.modules.security.oauth2;

import com.timeworx.common.constant.Constant;
import com.timeworx.common.utils.HttpContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 过滤
 * @Author: ryzhang
 * @Date 2023/2/3 11:30 PM
 */
public class Oauth2Filter extends AuthenticatingFilter {

    /**
     * 需要优先登陆，登陆失败仍可以访问的接口列表
     */
    static List<String> interfaceList = new ArrayList<String>(){
        {
            // 活动列表 和 活动详情
            add("/timeworx/event/qryDetail");
            add("/timeworx/event/qryList");
        }
    };

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isBlank(token)){
            // 没有token，走登陆失败逻辑，防止内部异常
            token = "-1";
        }
        return new Oauth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 访问拒绝 尝试获取token登陆
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            // 特定方法 登陆失败可继续访问
            String returnUrl = ((HttpServletRequest)request).getRequestURI();
            if(interfaceList.contains(returnUrl)){
                return true;
            }
            // 处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            String json = "login failed";
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
        }
        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader(Constant.TOKEN_HEADER);

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter(Constant.TOKEN_HEADER);
        }

        return token;
    }

}