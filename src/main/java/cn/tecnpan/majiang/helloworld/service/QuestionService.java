package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;

public interface QuestionService {

    void createQuestion(Question question);

    PaginationDto<QuestionDto> getList(Integer pageNo, Integer pageSize);

    PaginationDto<QuestionDto> list(Integer id, Integer pageNo, Integer pageSize);

    QuestionDto getById(Integer id);

    void createOrUpdate(Question question);
}
