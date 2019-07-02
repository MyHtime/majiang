package cn.tecnpan.majiang.helloworld.mapper;

import cn.tecnpan.majiang.helloworld.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified, avatar_url) " +
            "values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User selectByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User selectById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set token = #{user.token}, " +
                            "name = #{user.name}, " +
                            "avatar_url = #{user.avatarUrl}, " +
                            "gmt_modified = #{user.gmtModified} " +
                "where account_id = #{user.accountId}")
    void update(@Param("user") User user);
}
