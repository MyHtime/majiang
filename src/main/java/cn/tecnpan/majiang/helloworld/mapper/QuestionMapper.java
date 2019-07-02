package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.dto.QuestionDto;
import cn.tecnpan.majiang.helloworld.model.Question;
import cn.tecnpan.majiang.helloworld.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag)" +
            "values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void insert(Question question);

    /**
     * 如果需要传递过个参数 column="{"key"=列名,"key"=列名}"  key自定义，单个参数不要这么写
     * 另一个查询中获取传递过来的参数  #{key}
     * SQL分页(原生)
     * @param offset 偏移量
     * @param pageSize 每页数量
     */
    @Select("select * from question limit #{offset}, #{pageSize}")
    @Results(id = "questionDto", value = {
//            @Result(id = true, column = "id", property = "id"),
//            @Result(column = "title", property = "title"),
//            @Result(column = "description", property = "description"),
//            @Result(column = "tag", property = "tag"),
//            @Result(column = "gmt_create", property = "gmtCreate"),
//            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "creator", property = "creator"),
//            @Result(column = "view_count", property = "viewCount"),
//            @Result(column = "comment_count", property = "commentCount"),
//            @Result(column = "like_count", property = "likeCount"),
            @Result(column = "creator", property = "user", javaType = User.class, one = @One(select = "cn.tecnpan.majiang.helloworld.mapper.UserMapper.selectById"))
    })
    List<QuestionDto> list(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{id} limit #{offset}, #{pageSize}")
    @ResultMap("questionDto")
    List<QuestionDto> listByUserId(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("id") Integer id);

    @Select("select count(1) from question where creator = #{id}")
    Integer countByUserId(@Param("id") Integer id);

    @Select("select * from question where id = #{id}")
    @ResultMap("questionDto")
    QuestionDto findById(@Param("id") String id);
}
