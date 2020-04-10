//package com.taest.tourismdatavisualization.listener;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.http.HttpSession;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 创建ServletContext监听
// * 在监听中创建访问人数的map集合,数据存入ServletContext中
// */
//@Component
//public class ServletContextListener implements javax.servlet.ServletContextListener {
//    private Map<String, List<HttpSession>> userMap = new HashMap<>();
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        sce.getServletContext().setAttribute("userMap",userMap);
//    }
//}
