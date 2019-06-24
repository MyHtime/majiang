package cn.tecnpan.majiang.helloworld.dto;

import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto extends Question {

    private User user;
}
