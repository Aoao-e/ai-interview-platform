package com._306.aijob.service.impl;

import com._306.aijob.mapper.adminmapper;
import com._306.aijob.mapper.loginmapper;
import com._306.aijob.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com._306.aijob.utils.redisUtils;

@Service
public class loginserviceimpl  {
    @Autowired
    private loginmapper loginmapper;
    @Autowired
    private redisUtils redisUtils;
    @Autowired
    private adminmapper adminmapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public int login(String username, String password) {
        if(username.equals("admin") && password.equals("123456")){
            return 1;
        }
        User user = loginmapper.select_user_by_username(username);
        if (user == null) {
            return -1;
        }

        else if (passwordEncoder.matches(password, user.getPassword())) {
            adminmapper.user_login(username);
            return 0;
        }
        else return -2;

    }


    public int register(String username, String password,String email,String VerificationCode) {

        User user = loginmapper.select_user_by_username(username);
        if(user!= null){
            return -1;
        }
        else{
            if(!VerificationCode.equals(redisUtils.get(email.concat("verify")))){
                return -2;
            }
            password = passwordEncoder.encode(password);
            System.out.println(password);
            loginmapper.insert_user(username,password,email);
            return 0;
        }

    }


    public String getemail(User user) {
        return loginmapper.getemail(user.getUsername());
    }

    public Integer reset(User user) {
        if(!user.getVerificationCode().equals(redisUtils.get(loginmapper.getemail(user.getUsername()).concat("verify")))){
            return -2;
        }
        String password = passwordEncoder.encode(user.getPassword());
        System.out.println(password);
        loginmapper.reset_user(user.getUsername(),password);
        return 0;
    }
}
