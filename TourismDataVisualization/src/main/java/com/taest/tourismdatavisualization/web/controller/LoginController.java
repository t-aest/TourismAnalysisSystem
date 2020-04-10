package com.taest.tourismdatavisualization.web.controller;

import com.taest.tourismdatavisualization.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description:
 * @Author: Taest
 * @CreateDate: 2020/4/2$ 11:43$
 */
@Controller
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/verify") //    @RequestMapping(value =
    public String verify(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         Map<String, Object> map,
                         HttpSession session){
        if ( !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {

            //查询数据库是否存在
            Integer flag = userMapper.checkUser(username, password);
            if (flag!=null && flag==1){
                //登录成功，
                // 防止表单重复提交，通过重定向到主页, 需要添加一个视图
                session.setAttribute("loginUser", username);
                return "redirect:/main";
            }

        }
        //登录失败
        map.put("msg", "用户名或密码错误！");
        return "login";
    }

    @RequestMapping("/reset")
    public String reset(){
        return "reset";
    }

    @GetMapping("/repassword/pwd")
    @ResponseBody
    public Boolean repassword(@RequestParam("username") String username,
                              @RequestParam("password") String password){
        System.out.println(username + password);
        if ( !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {

            //查询数据库是否存在
            Integer flag = userMapper.checkUser(username, password);
            if (flag!=null && flag==1){
                return true;
            }

        }
        return false;

    }

    @PostMapping("/updatepassword")
    public String updatePwd(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Map<String, Object> map) {

        Integer flag = userMapper.updatePwd(username, password);

        if (flag!=null && flag==1){
            map.put("msg", "密码修改成功！");
            return "redirect:/login";
        }
        map.put("msg", "密码修改成功！");
        return "redirect:/login";
    }

}
