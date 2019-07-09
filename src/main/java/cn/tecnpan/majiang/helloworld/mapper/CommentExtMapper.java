package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.model.Comment;
import org.apache.ibatis.annotations.Param;

public interface CommentExtMapper {
    /**
     * 二级评论增加评论数
     */
    void incCommentCount(@Param("record") Comment comment);
}