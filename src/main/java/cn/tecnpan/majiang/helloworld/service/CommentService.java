package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.dto.CommentDto;
import cn.tecnpan.majiang.helloworld.enums.CommentTypeEnum;
import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.model.User;

import java.util.List;

public interface CommentService {
    void insert(Comment comment, User commentator);

    List<CommentDto> listByTargetId(Long id, CommentTypeEnum commentType);
}
