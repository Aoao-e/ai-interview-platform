package com._306.aijob.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class mailsendutils {
    @Autowired
    private JavaMailSender mailSender;
    @Async
    public void sendEmail(String to, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("interview_123@163.com");
        message.setTo(to);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code + "，5分钟内有效");
        mailSender.send(message);
    }
}
