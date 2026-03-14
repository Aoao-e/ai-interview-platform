package com._306.aijob.pojo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Result {

    private Integer code; //编码：1成功，0为失败
    private String msg; //错误信息
    private Object data; //数据
    private Boolean is_admin=false;
    private String username;
    private ArrayList<answerclass> answers;

    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = 1;
        result.msg = "success";
//        result.is_admin=false;
        return result;
    }

    public static Result success_admin(Object object,Boolean is_admin) {
        Result result = new Result();
        result.data = object;
        result.is_admin = is_admin;
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success_with_question_answer(ArrayList<answerclass> answers) {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.answers = answers;

        return result;
    }


    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }


}
