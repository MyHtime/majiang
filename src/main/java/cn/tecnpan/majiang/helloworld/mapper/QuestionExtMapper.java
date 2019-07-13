package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.model.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * QuestionMapper 的扩展Mapper
 * 自定义SQL
 */
public interface QuestionExtMapper {
    void incViewCount(@Param("record") Question question);

    void incCommentCount(@Param("record") Question question);

    /**
     * 查询对应tag的问题
     */
    List<Question> selectRelated(@Param("question") Question question);
}
