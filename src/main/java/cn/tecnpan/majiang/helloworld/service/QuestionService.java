package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;

import java.util.List;

public interface QuestionService {

    void createQuestion(Question question);

    List<QuestionDto> getList();
}
