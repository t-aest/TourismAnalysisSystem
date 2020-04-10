//package com.taest.tourismdatavisualization.listener;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletRequestEvent;
//import javax.servlet.ServletRequestListener;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * ServletContest监听
// * 在有请求访问站点时,获取session,
// * 通过request获取用户路由地址。
// * 把地址存入session中.
// * 判断Map中是否存在相同的ip,
// * 如果没有创建lsessionList,把ip和lsessionLst存入userMap中。
// * 如果存在相同的ip,则获取sessionList,把session存入.
// * 最后在把数据保存在userMap中。
// *
// */
//@Component
//public class RequestContextListener implements ServletRequestListener {
//    @Override
//    public void requestInitialized(ServletRequestEvent sre) {
//        ServletRequest servletRequest = sre.getServletRequest();
//        //获取ip,存入ServletContext
//        String ip = servletRequest.getRemoteAddr();
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        //获取session
//        HttpSession session = request.getSession();
//        session.setAttribute("ip",ip);
//        //获取map集合
//        ServletContext servletContext = sre.getServletContext();
//        Map<String, List<HttpSession>> userMap = (Map<String, List<HttpSession>>) servletContext.getAttribute("userMap");
//        //获取sessionList
//        List<HttpSession> sessionList=userMap.containsKey(ip) ? userMap.get(ip) : new ArrayList<>();
//        if(!sessionList.contains(session)){
//            sessionList.add(session);
//        }
//        userMap.put(ip, sessionList);
//        //存入userMap
//        servletContext.setAttribute("userMap",userMap);
//    }
//}