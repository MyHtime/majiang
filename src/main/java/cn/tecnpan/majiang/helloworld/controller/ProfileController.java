package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String question(@PathVariable(name = "action") String action,
                           Map<String, Object> map,
                           @SessionAttribute(name = "loginUser", required = false) User user,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            map.put("section", "questions");
            map.put("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            map.put("section", "replies");
            map.put("sectionName", "最新回复");
        }

        PaginationDto<QuestionDto> pagination = questionService.list(user.getId(), pageNo, pageSize);
        map.put("pagination", pagination);
        return "profile";
    }
}
