package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.code.impl.CustomizeErrorCodeImpl;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.tecnpan.majiang.helloworld.mapper.QuestionExtMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.mapper.UserMapper;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.QuestionExample;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.QuestionService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * QuestionMapper的扩展mapper即写自定义SQL，防止MBG执行时被覆盖
     */
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void createQuestion(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public PaginationDto<QuestionDto> getList(Integer pageNo, Integer pageSize) {
        PaginationDto<QuestionDto> pagination = new PaginationDto<>((int)questionMapper.countByExample(new QuestionExample()), pageSize);
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pagination.getTotalPage()) {
            pageNo = pagination.getTotalPage();
        }
        Integer offset = pageSize * (pageNo - 1);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, pageSize));
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pagination.setObjectList(questionDtoList);
        pagination.init(pageNo);
        return pagination;
    }

    /**
     * 根据UserId查询问题
     * @param userId userID
     */
    @Override
    public PaginationDto<QuestionDto> list(Integer userId, Integer pageNo, Integer pageSize) {
        PaginationDto<QuestionDto> pagination = new PaginationDto<>((int)questionMapper.countByExample(new QuestionExample()), pageSize);
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pagination.getTotalPage()) {
            pageNo = pagination.getTotalPage();
        }
        Integer offset = pageSize * (pageNo - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, pageSize));
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pagination.setObjectList(questionDtoList);
        pagination.init(pageNo);
        return pagination;
    }

    @Override
    public QuestionDto getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        questionDto.setUser(user);
        return questionDto;
    }

    @Override
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());

            Question questionRecord = new Question();
            questionRecord.setGmtModified(System.currentTimeMillis());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int row = questionMapper.updateByExampleSelective(questionRecord, questionExample);
            if (row == 0) {
                throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FOUND);
            }
        }
    }

    /**
     * 增加阅读数
     * @param id question id
     */
    @Override
    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
