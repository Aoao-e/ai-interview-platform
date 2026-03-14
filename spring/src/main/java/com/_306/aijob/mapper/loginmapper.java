package com._306.aijob.mapper;

import com._306.aijob.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface loginmapper {

    @Select("select * from user where username=#{username}")
    User select_user_by_username(@Param("username") String username);

    @Insert("insert into user(username,password,email) values(#{username},#{password},#{email})")
    void insert_user(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    @Select("select email from user where username=#{username}")
    String getemail(@Param("username") String username);

    @Select("update user set password =#{password} where username=#{username}")
    void reset_user(@Param("username") String username, @Param("password") String password);

}
