package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag)" +
            "values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void insert(Question question);

    /**
     * 如果需要传递过个参数 column="{"key"=列名,"key"=列名}"  key自定义
     * 另一个查询中获取传递过来的参数  #{key}
     */
    @Select("select * from question")
    @Results(id = "questionDto", value = {
//            @Result(id = true, column = "id", property = "id"),
//            @Result(column = "title", property = "title"),
//            @Result(column = "description", property = "description"),
//            @Result(column = "tag", property = "tag"),
//            @Result(column = "gmt_create", property = "gmtCreate"),
//            @Result(column = "gmt_modified", property = "gmtModified"),
//            @Result(column = "creator", property = "creator"),
//            @Result(column = "view_count", property = "viewCount"),
//            @Result(column = "comment_count", property = "commentCount"),
//            @Result(column = "like_count", property = "likeCount"),
            @Result(column = "creator", property = "user", javaType = User.class, one = @One(select = "cn.tecnpan.majiang.helloworld.mapper.UserMapper.selectById"))
    })
    List<QuestionDto> list();
}
