package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.CommentDto;
import cn.tecnpan.majiang.helloworld.enums.CommentTypeEnum;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.tecnpan.majiang.helloworld.mapper.CommentMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionExtMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.mapper.UserMapper;
import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.model.CommentExample;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.model.UserExample;
import cn.tecnpan.majiang.helloworld.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 增加评论
     *
     * @param comment 评论
     */
    @Override
    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorEnum.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorEnum.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //回复评论
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andParentIdEqualTo(comment.getParentId());
            List<Comment> commentList = commentMapper.selectByExample(commentExample);
            if (commentList == null) {
                throw new CustomizeException(CustomizeErrorEnum.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorEnum.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            Question record = new Question();
            record.setCommentCount(1);
            record.setId(question.getId());
            questionExtMapper.incCommentCount(record);
        }
    }

    /**
     * 拿出当前问题下的所有评论并携带对应的user信息
     * @param id 问题id
     */
    @Override
    public List<CommentDto> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        if (commentList.size() == 0) {
            return new ArrayList<>();
        }

        //lambdas表达式获取去重的评论人的ID -> 转化为ID列表
        List<Long> userIds = commentList.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        //lambdas表达式将评论人转化为Map<Long, User>
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));

        //lambdas表达式组装CommentDto
        return commentList.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            commentDto.setUser(userMap.get(comment.getCommentator()));
            return commentDto;
        }).collect(Collectors.toList());
    }
}
