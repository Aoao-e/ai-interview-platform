package com._306.aijob.service.impl;

import com._306.aijob.mapper.adminmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class adminserviceimpl {
    @Autowired
    adminmapper adminmapper;

    public Map<String, String> get_admindata() {
        Map<String, String> map=new HashMap();

        map.put("today_count",adminmapper.today_count().toString());
        map.put("answer_total",adminmapper.answer_total().toString());
        map.put("user_total",adminmapper.user_total().toString());
        map.put("star_count",adminmapper.star_count().toString());
        map.put("hate_count",adminmapper.hate_count().toString());
        map.put("question_count",adminmapper.question_count().toString());

        return map;
    }
}
