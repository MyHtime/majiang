package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void createQuestion(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public PaginationDto<QuestionDto> getList(Integer pageNo, Integer pageSize) {
        PaginationDto<QuestionDto> pagination = new PaginationDto<>(questionMapper.count(), pageSize);
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pagination.getTotalPage()) {
            pageNo = pagination.getTotalPage();
        }
        Integer offset = pageSize * (pageNo - 1);
        pagination.setObjectList(questionMapper.list(offset, pageSize));
        pagination.init(pageNo);
        return pagination;
    }

    /**
     * 根据UserId查询问题
     * @param id userID
     */
    @Override
    public PaginationDto<QuestionDto> list(Integer id, Integer pageNo, Integer pageSize) {
        PaginationDto<QuestionDto> pagination = new PaginationDto<>(questionMapper.countByUserId(id), pageSize);
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pagination.getTotalPage()) {
            pageNo = pagination.getTotalPage();
        }
        Integer offset = pageSize * (pageNo - 1);
        pagination.setObjectList(questionMapper.listByUserId(offset, pageSize, id));
        pagination.init(pageNo);
        return pagination;
    }

    @Override
    public QuestionDto getById(Integer id) {
        return questionMapper.findById(id);
    }

    @Override
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
