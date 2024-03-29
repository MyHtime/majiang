package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.cache.TagCache;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tagDaoList", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id, Model model) {

        QuestionDto questionDto = questionService.getById(id);
        model.addAttribute("id", questionDto.getId());
        model.addAttribute("tag", questionDto.getTag());
        model.addAttribute("title", questionDto.getTitle());
        model.addAttribute("description", questionDto.getDescription());
        model.addAttribute("tagDaoList", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name = "title") String title,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "tag") String tag,
                            @RequestParam(name = "id", required = false) Long id,
                            @SessionAttribute(name = "loginUser", required = false) User user,
                            Model model) {
        model.addAttribute("tag", tag);
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tagDaoList", TagCache.get());
        if (user == null) {
            model.addAttribute("error", "用户未登录！");
            return "publish";
        }
        if (title == null || title.equals("")) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error", "描述不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalidTags(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签：" + invalid);
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
