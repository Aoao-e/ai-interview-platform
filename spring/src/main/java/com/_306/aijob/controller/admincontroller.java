package com._306.aijob.controller;

import com._306.aijob.pojo.Result;
import com._306.aijob.pojo.User;

import com._306.aijob.service.impl.adminserviceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class admincontroller {
    @Autowired
    private adminserviceimpl adminserviceimpl;
    @PostMapping("/admin")
    public Result get_admin(@RequestBody User user)  {
        System.out.println("get_admin");
        return Result.success("");

    }


    @GetMapping("/admin/stats")
    public Result get_admindata()  {
        System.out.println("get_admin");
        Map<String,String> admin_data= adminserviceimpl.get_admindata();
        return Result.success(admin_data);

    }
}