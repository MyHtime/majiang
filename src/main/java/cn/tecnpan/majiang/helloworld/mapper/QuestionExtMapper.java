package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.model.Question;
import org.apache.ibatis.annotations.Param;

/**
 * QuestionMapper 的扩展Mapper
 * 自定义SQL
 */
public interface QuestionExtMapper {
    void incView(@Param("record") Question question);
}