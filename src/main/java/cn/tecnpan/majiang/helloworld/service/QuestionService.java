package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;

import java.util.List;

public interface QuestionService {

    void createQuestion(Question question);

    PaginationDto<QuestionDto> getList(Integer pageNo, Integer pageSize);

    PaginationDto<QuestionDto> list(Integer id, Integer pageNo, Integer pageSize);

    QuestionDto getById(String id);
}
