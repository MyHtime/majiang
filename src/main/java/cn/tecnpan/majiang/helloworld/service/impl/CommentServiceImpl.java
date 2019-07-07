package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.enums.CommentTypeEnum;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.tecnpan.majiang.helloworld.mapper.CommentMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionExtMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.model.CommentExample;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 增加评论
     *
     * @param comment 评论
     */
    @Override
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
}
