package com.example.MyNetWork.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


public interface EmailService {
    public void sendEmail(String receiver,String topic,String massage);
    public void setJavaMailSender(JavaMailSender javaMailSender);
    public JavaMailSender getJavaMailSender();
}