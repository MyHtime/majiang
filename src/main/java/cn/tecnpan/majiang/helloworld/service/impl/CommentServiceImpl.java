package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.CommentDto;
import cn.tecnpan.majiang.helloworld.enums.CommentTypeEnum;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.enums.NotificationStatusEnum;
import cn.tecnpan.majiang.helloworld.enums.NotificationTypeEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.tecnpan.majiang.helloworld.mapper.CommentExtMapper;
import cn.tecnpan.majiang.helloworld.mapper.CommentMapper;
import cn.tecnpan.majiang.helloworld.mapper.NotificationMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionExtMapper;
import cn.tecnpan.majiang.helloworld.mapper.QuestionMapper;
import cn.tecnpan.majiang.helloworld.mapper.UserMapper;
import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.model.CommentExample;
import cn.tecnpan.majiang.helloworld.model.Notification;
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

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 增加评论
     *
     * @param comment 评论（来自html，其parentId为父级内容的ID）
     * @param commentator 评论者
     */
    @Override
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorEnum.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorEnum.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //查询父类内容
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorEnum.COMMENT_NOT_FOUND);
            }

            //获取回复评论的对应的问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorEnum.QUESTION_NOT_FOUND);
            }


            //回复评论
            commentMapper.insert(comment);
            //评论数增加
            Comment parentComment = new Comment();
            parentComment.setCommentCount(1);
            parentComment.setId(comment.getParentId());
            commentExtMapper.incCommentCount(parentComment);
            //完成回复评论，添加通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorEnum.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //评论数增加
            Question record = new Question();
            record.setCommentCount(1);
            record.setId(question.getId());
            questionExtMapper.incCommentCount(record);
            //完成回复问题，添加通知
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    /**
     * 创建通知
     * @param comment 一条评论对象
     * @param receiver 接收通知的人
     * @param notifierName 发出通知的人
     * @param outerTitle 回复/评论的 内容
     * @param notificationType 通知类型
     * @param outerId 被评论问题的id
     */
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver.equals(comment.getCommentator())) {
            //通知人和评论人相同
            return;
        }
        Notification notify = new Notification();
        notify.setGmtCreate(System.currentTimeMillis());
        notify.setType(notificationType.getType());
        //被评论问题的ID
        notify.setOuterId(outerId);
        //被评论问题的标签
        notify.setOuterTitle(outerTitle);
        //发起通知者ID（评论者ID）
        notify.setNotifier(comment.getCommentator());
        //发起通知者的名字
        notify.setNotifierName(notifierName);
        //通知接收者ID（问题创建者）
        notify.setReceiver(receiver);
        notify.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notify);
    }

    /**
     * 拿出当前问题下的所有评论并携带对应的user信息
     * @param id 问题id
     * @param commentType 1级评论or2级评论的枚举类型(父类id的类型)
     */
    @Override
    public List<CommentDto> listByTargetId(Long id, CommentTypeEnum commentType) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(commentType.getType());
        //按照创建时间排序
        commentExample.setOrderByClause("gmt_create desc");
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
