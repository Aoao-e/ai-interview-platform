package com._306.aijob.utils;

public class RandomVerificationCode{
    public  String getRandomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int number = (int) (Math.random() * 10);
            sb.append(number);
        }
        return sb.toString();
    }
}
