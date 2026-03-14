package com._306.aijob.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/*
  - 提问数：questionCount
  - 收藏数：starCount
  - 点踩数：hateCount
 */
@Mapper
public interface adminmapper {
    @Insert("INSERT INTO user_login (username, date) SELECT #{username}, NOW() WHERE NOT EXISTS (SELECT 1 FROM user_login WHERE username = #{username} AND date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY);")
    void user_login(@Param("username")String username);

    @Select("SELECT count(*) FROM user_login WHERE date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY")
    Integer today_count();

    @Select("SELECT count(*) FROM user ")
    Integer user_total();

    @Select("SELECT count(*) FROM question WHERE date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY")
    Integer answer_total();

    @Select("SELECT count(DISTINCT username, date) FROM question WHERE date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY ")
    Integer question_count();

    @Select("SELECT count(*) FROM question WHERE date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY and star=1")
    Integer star_count();

    @Select("SELECT count(*) FROM question WHERE date >= CURDATE() AND date < CURDATE() + INTERVAL 1 DAY and hate=1")
    Integer hate_count();


}
