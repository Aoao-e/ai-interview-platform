package com._306.aijob.mapper;

import com._306.aijob.pojo.answerclass;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface userservicemapper {

    void insert_question(answerclass answerclass);

    void hate_question(@Param("question_id") Integer question_id);

    Integer is_hate(@Param("question_id") Integer question_id);

    void star_question(@Param("question_id") Integer question_id);

    Integer is_star(@Param("question_id") Integer question_id);

    ArrayList<answerclass> get_questions(@Param("username") String username,
                                         @Param("start_count") Integer start_count);

    ArrayList<answerclass> get_questionssearch(@Param("username") String username,
                                               @Param("search") String search,
                                               @Param("start_count") Integer start_count);

    Integer countquestion(@Param("username") String username);

    Integer countquestionsearch(@Param("username") String username,
                                @Param("search") String search);

    Integer counthistory(@Param("username") String username);

    Integer counthistorynsearch(@Param("username") String username,
                                @Param("search") String search);

    ArrayList<answerclass> get_history(@Param("username") String username,
                                       @Param("start_count") Integer start_count);

    ArrayList<answerclass> get_historysearch(@Param("username") String username,
                                             @Param("search") String search,
                                             @Param("start_count") Integer start_count);

    void insert_question_requirement(Integer questionId, String job, String tone, String extrainfo);
}
//public interface userservicemapper {


//    @Insert("insert into user_question(username,question,answer,job_name,tone,extrainfo) values(#{username},#{question},#{answer},#{job_name},#{tone},#{extrainfo})")
//    void insert_user_question(@Param("username") String username, @Param("question") String question, @Param("answer") String answer,@Param("job_name") String job_name,@Param("tone") String tone,@Param("extrainfo") String extrainfo);
//
//    @Update("UPDATE user_question set hate=1 -hate where question=(#{question}) and username=(#{username})")
//    void hate_question(@Param("question")String question, @Param("username")String username);
//
//    @Select("select hate from user_question where question=(#{question}) and username=(#{username})")
//    Integer is_hate(@Param("question")String question, @Param("username")String username);
//
//    @Update("UPDATE user_question set star=1-star where question=(#{question}) and username=(#{username})")
//    void star_question(@Param("question")String question, @Param("username")String username);
//
//    @Select("select star from user_question where question=(#{question}) and username=(#{username})")
//    Integer is_star(@Param("question")String question, @Param("username")String username);
//
//
//    @Select("select question,answer from user_question where username=#{username} and star=1 limit #{start_count},5")
//    List<Map<String,String>> get_questions(@Param("username")String username,@Param("start_count")Integer start_count);
//
//    @Select("select question,answer from user_question " +
//            "where username=#{username} " +
//            "and question like CONCAT('%',#{search},'%') " +
//            "and star=1 limit #{start_count},5")
//    List<Map<String, String>> get_questionssearch(@Param("username")String username,@Param("search")String search,@Param("start_count")Integer start_count);
//
//    @Select("select count(*) from user_question where  username=(#{username}) and star=1")
//    Integer countquestion(@Param("username") String username);
//
//    @Select("select count(*) from user_question where  username=(#{username})and question like CONCAT('%',#{search},'%') and star=1")
//    Integer countquestionsearch(@Param("username") String username,@Param("search")String search);
//
//
//    @Select("select count(*) from user_question where  username=(#{username}) ")
//    Integer counthistory(@Param("username") String username);
//
//    @Select("select count(*) from user_question where  username=(#{username}) and question like CONCAT('%',#{search},'%') ")
//    Integer counthistorynsearch(@Param("username") String username, @Param("search")String search);
//
//
//    @Select("select question,answer from user_question where username=#{username}  limit #{start_count},5")
//    List<Map<String,String>> get_history(@Param("username")String username,@Param("start_count")Integer start_count);
//
//    @Select("select question,answer from user_question where username=#{username} and question like CONCAT('%',#{search},'%') limit #{start_count},5")
//    List<Map<String,String>> get_historysearch(@Param("username")String username,@Param("search")String search,@Param("start_count")Integer start_count);
//}
