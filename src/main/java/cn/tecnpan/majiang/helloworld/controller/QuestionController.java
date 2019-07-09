package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.CommentDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.enums.CommentTypeEnum;
import cn.tecnpan.majiang.helloworld.service.CommentService;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /**
     * 查看某个问题的详情，包括它的一级评论列表
     * @param id 问题的id
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model) {
        QuestionDto questionDto = questionService.getById(id);
        //每成功访问一次，增加一次阅读量
        questionService.incView(id);
        model.addAttribute("question", questionDto);

        //获取评论列表，并携带user信息
        List<CommentDto> commentDtoList = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments", commentDtoList);
        return "question";
    }
}
