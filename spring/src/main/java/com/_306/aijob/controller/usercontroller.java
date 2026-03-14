package com._306.aijob.controller;


import com._306.aijob.pojo.*;
import com._306.aijob.service.impl.usersserviceimpl;
import com._306.aijob.utils.jwt.JwtUtils;
import com._306.aijob.utils.redisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.UUID;
@EnableAsync
@RestController
public class usercontroller {
    @Autowired
    private usersserviceimpl usersserviceimpl;
    @Autowired
    private redisUtils redisUtils;
    @Autowired
    JwtUtils JwtUtils;
    @Autowired
    private DataRedisConnectionDetails dataRedisConnectionDetails;

    @PostMapping("/user/qa")
    public Result login(){
        System.out.println("qa");
        return Result.success("123");
    }

    @PostMapping("/user/sendquestion")
    public Result sendquestion(@RequestBody requirements requirements) throws InterruptedException {

        String service_number=UUID.randomUUID().toString();
        if(redisUtils.get(requirements.getUsername())!=null){
            return Result.error("正在生成中,请勿频繁点击...");
        }
        redisUtils.set(requirements.getUsername(),service_number);

        ArrayList<answerclass> answers=usersserviceimpl.get_question_answer(requirements,service_number);
        redisUtils.delete(requirements.getUsername());

        return Result.success_with_question_answer(answers);
    }
    @PostMapping("/user/sendfeedback")
    public Result sendfeedback(@RequestBody feedback feedback)  {
        System.out.println("feedback");

        return usersserviceimpl.solve_feedback(feedback);
    }
    @PostMapping ("/user/notebook")
    public Result notebook(@RequestBody page page )  {
        ArrayList<answerclass> map=usersserviceimpl.solve_notebook(page);

        return Result.success_with_question_answer(map);
    }
    @PostMapping ("/user/notebookcount")
    public Result notebookcount(@RequestBody page page )  {
        Integer count= usersserviceimpl.notebookcount(page);


        return Result.success(count);
    }
    @PostMapping("/user/searchstarhate")
    public Result search_is_star_hate(@RequestBody feedback feedback){
        System.out.println("search_is_star_hate");
        if(feedback.getIs_search()==1) {
            int[] list = usersserviceimpl.search_is_star_hate(feedback);
            return Result.success(list);
        }
        else return Result.error("not search");
    }
    @PostMapping("/user/history")
    public Result get_history(@RequestBody page page )  {
        System.out.println("get_history");
        ArrayList<answerclass> map=usersserviceimpl.get_history(page);

        return Result.success_with_question_answer(map);
    }

    @PostMapping("/user/historycount")
    public Result historycount(@RequestBody page page )  {
        Integer count= usersserviceimpl.historycount(page);
        return Result.success(count);
    }

}
