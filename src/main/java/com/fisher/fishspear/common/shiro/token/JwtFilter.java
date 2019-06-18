package com.fisher.fishspear.common.shiro.token;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Li
 * @create 2018-07-12 15:56
 * @desc
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    public static final String TOKEN_HEADER = "token";

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(TOKEN_HEADER);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(TOKEN_HEADER);

        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (AuthenticationException e) {
                return response(response);
            }
        }
        return true;
    }

    private boolean response(ServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Map map = new HashMap();
        map.put("state", "505");
        map.put("message", "Authentication Required");
        map.put("data", "");
        response.resetBuffer();
        try {
            OutputStream os = response.getOutputStream();
            os.write(JSON.toJSONString(map).getBytes());
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}

