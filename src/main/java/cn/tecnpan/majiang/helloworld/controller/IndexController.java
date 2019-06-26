package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    /**
     * 访问主页，免登陆
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
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

        List<QuestionDto> questionDtoList = questionService.getList();
        for (QuestionDto questionDto : questionDtoList) {
            questionDto.setDescription("description");
        }
        model.addAttribute("questions", questionDtoList);
        return "index";
    }
}
