package cn.tecnpan.majiang.helloworld.interceptor;

import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("loginUser") != null) {
            return true;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
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
        }
        return true;
    }
}
