package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    /**
     * 访问主页，免登陆
     */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "index";
        }
        for (Cookie cookie : cookies) {
            //获取浏览器的token
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                User user = userService.findUserByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("loginUser", user);
                }
                break;
            }
        }
        return "index";
    }
}
