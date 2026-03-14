package com._306.aijob.service.impl;

import com._306.aijob.mapper.userservicemapper;
import com._306.aijob.pojo.*;
import com._306.aijob.utils.redisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class usersserviceimpl  {
    @Autowired
    private redisUtils redisUtils;
    @Autowired
    private userservicemapper userservicemapper;


    public ArrayList<answerclass> get_question_answer(requirements requirements, String service_number) throws InterruptedException {


//        redisUtils.set(service_number,requirements);

        redisUtils.push_task(service_number, requirements);
        System.out.println("has pushed task" + service_number);
        String question_answer_string = redisUtils.gettask(service_number);
        System.out.println("userserviceimpl" + question_answer_string);
        ArrayList<answerclass> answers=new ArrayList<>();
        for (int i = 1; i <= requirements.getCount(); i++) {
            //将redis中的问题与回答封装到answerclass

            int start = question_answer_string.indexOf("question:") + "question:".length();
            int end = question_answer_string.indexOf("answer:");
            String map_question = question_answer_string.substring(start, end).trim();
            question_answer_string = question_answer_string.substring(end).trim();
            start = "answer:".length();
            end = question_answer_string.indexOf("}");
            String map_answer = question_answer_string.substring(start, end).trim();
            System.out.println("question:" + map_question + "answer:" + map_answer);
            question_answer_string = question_answer_string.substring(end).trim();
            answerclass answerclass =new answerclass();
            answerclass.setUsername(requirements.getUsername());
            answerclass.setQuestion(map_question);
            answerclass.setAnswer(map_answer);
            answerclass.setCount(i);
            //将answerclass记录到数据库并返回前端
            userservicemapper.insert_question(answerclass);
            userservicemapper.insert_question_requirement(answerclass.getQuestionId(),

                    requirements.getJob(),
                    requirements.getTone(),
                    requirements.getExtraInfo()
                    );
            answers.add(answerclass);

        }

        return answers;
    }



    public Result solve_feedback(feedback feedback) {

        if ("hate".equals(feedback.getFeedback())) {
            userservicemapper.hate_question(feedback.getQuestion_id());
            int r = userservicemapper.is_hate(feedback.getQuestion_id());
            return Result.success(r);
        }
        if ("star".equals(feedback.getFeedback())) {
            userservicemapper.star_question( feedback.getQuestion_id());
            Integer r = userservicemapper.is_star(feedback.getQuestion_id());
            return Result.success(r);
        }
        else return  Result.error("not hate or star");
    }

    public int[] search_is_star_hate(feedback feedback){
        System.out.println("search_is_star_hate feedback:"+feedback.getQuestion_id());
        Integer is_star = userservicemapper.is_star(feedback.getQuestion_id());
        Integer is_hate = userservicemapper.is_hate(feedback.getQuestion_id());

        return new  int[]{is_star,is_hate};
    }

    public ArrayList<answerclass> solve_notebook(page page){
        ArrayList<answerclass> list=new ArrayList<>();
        if (page.getSearch()!=null){
            list= userservicemapper.get_questionssearch(page.getUsername(),page.getSearch(),(page.getPage()-1)*5);
        }
        else{
            list= userservicemapper.get_questions(page.getUsername(),(page.getPage()-1)*5);
        }
        return list;
    }

    public Integer notebookcount(page page) {
        if (page.getSearch()!=null){
            return userservicemapper.countquestionsearch(page.getUsername(),page.getSearch());
        }
        return userservicemapper.countquestion(page.getUsername());
    }
    public ArrayList<answerclass> get_history(page page){
        ArrayList<answerclass> list = new ArrayList<>();
        if (page.getSearch()!=null){
            list= userservicemapper.get_historysearch(page.getUsername(),page.getSearch(),(page.getPage()-1)*5);

        }
        else{
             list= userservicemapper.get_history(page.getUsername(),(page.getPage()-1)*5);


        }
        for(answerclass answerclass : list){
            String question = answerclass.getQuestion();
            Integer question_id = answerclass.getQuestionId();
            System.out.println("question:" + question + "question:" + question_id);
        }

        return list;
    }

    public Integer historycount(page page) {
        if (page.getSearch()!=null){
            return userservicemapper.counthistorynsearch(page.getUsername(),page.getSearch());
        }
        return userservicemapper.counthistory(page.getUsername());
    }

}
