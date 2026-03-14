package com._306.aijob.controller;

import com._306.aijob.pojo.Result;
import com._306.aijob.pojo.User;
import com._306.aijob.service.impl.loginserviceimpl;
import com._306.aijob.utils.jwt.JwtUtils;
import com._306.aijob.utils.mailsendutils;
import com._306.aijob.utils.RandomVerificationCode;
import com._306.aijob.utils.redisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

@EnableAsync
@RestController
public class logincontroller {
    @Autowired
    private loginserviceimpl loginserviceimpl;
    @Autowired
    private mailsendutils mailsendutils;
    @Autowired
    private redisUtils redisUtils;
    @Autowired
    JwtUtils JwtUtils;

    @PostMapping("/login")

    public Result login(@RequestBody User user) {
        int code = loginserviceimpl.login(user.getUsername(), user.getPassword());
        if(code==1){
            String token = JwtUtils.generateAdminToken(user.getUsername());
            return Result.success_admin(token,true);
        }
        if (code == 0) {

            String token = JwtUtils.generateToken(user.getUsername());
            return Result.success(token);

        } else if (code==-1) {
            return Result.error("用户名不存在");
        }
        else return Result.error("密码错误");
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
//        System.out.println(user.getUsername()); System.out.println(user.getPassword());System.out.println(user.getEmail());
        int code = loginserviceimpl.register(user.getUsername(), user.getPassword(), user.getEmail(), user.getVerificationCode());
        if (code == 0) {
            String token = JwtUtils.generateToken(user.getUsername());
            return Result.success(token);
        }
        else if (code==-1) {
            return Result.error("用户名已存在");
        }
        else return Result.error("验证码错误");
    }

    @PostMapping("/getcode")
    public Result getcode(@RequestBody User user) {

        if(user.getIs_forget()!=null){

        if(user.getIs_forget()==1){
            System.out.println("forget1"+user.getUsername());
            user.setEmail(loginserviceimpl.getemail(user));
        }}
        System.out.println(user.getEmail());
        try {
            String VerificationCode = new RandomVerificationCode().getRandomCode();
            mailsendutils.sendEmail(user.getEmail(), VerificationCode);
            if (redisUtils.get(user.getEmail().concat("verify")) != null) {
                return Result.error("验证码已发送，请勿频繁点击");
            }
            redisUtils.set(user.getEmail().concat("verify"), VerificationCode);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error("发送失败:"+e);
        }

    }

    @PostMapping("/forget")
    public Result forget(@RequestBody User user) {
        Integer code = loginserviceimpl.reset(user);
        if (code == 0) {
            return  Result.success();
        }
        else if (code==-2) {
            return Result.error("验证码错误");
        }
        else {
            return Result.error("重置密码失败");
        }
    }

    



}
