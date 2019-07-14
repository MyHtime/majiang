package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.NotificationDto;
import cn.tecnpan.majiang.helloworld.enums.NotificationTypeEnum;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String readNotify(@PathVariable("id") Long id,
                    @SessionAttribute(name = "loginUser", required = false) User user) {
        if (user == null) {
            return "redirect:/";
        }
        NotificationDto notificationDto = notificationService.readNotification(id, user);
        if (NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDto.getType() || NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDto.getType()) {
            return "redirect:/question/" + notificationDto.getOuterId();
        } else {
            return "redirect:/";
        }
    }
}
