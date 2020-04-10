package com.taest.tourismdatavisualization.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: Taest
 * @CreateDate: 2020/4/2$ 13:56$
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser != null) {
            //已经登录，放行请求
            return true;
        }
        //未登录, 转发到登录页面
        request.setAttribute("msg", "无权限，请登录后访问！");
        request.getRequestDispatcher("/login").forward(request, response);
        return false;

    }
}
