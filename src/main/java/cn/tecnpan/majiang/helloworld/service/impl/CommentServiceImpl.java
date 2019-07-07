package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.mapper.CommentMapper;
import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 增加评论
     * @param comment 评论
     */
    @Override
    public void insert(Comment comment) {
        commentMapper.insertSelective(comment);
    }
}
