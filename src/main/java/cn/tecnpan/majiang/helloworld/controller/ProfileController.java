package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String question(@PathVariable(name = "action") String action,
                           Map<String, Object> map,
                           HttpServletRequest request,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        if ("questions".equals(action)) {
            map.put("section", "questions");
            map.put("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            map.put("section", "replies");
            map.put("sectionName", "最新回复");
        }
        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                //获取浏览器的token
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user= userService.findUserByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("loginUser", user);
                    }
                    break;
                }
            }
        }
        if (user == null) {
            return "redirect:/";
        }
        PaginationDto<QuestionDto> pagination = questionService.list(user.getId(), pageNo, pageSize);
        map.put("pagination", pagination);
        return "profile";
    }
}
