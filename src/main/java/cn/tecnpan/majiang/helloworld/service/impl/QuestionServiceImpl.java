package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void createQuestion(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public List<QuestionDto> getList() {
        return questionMapper.list();
    }
}
