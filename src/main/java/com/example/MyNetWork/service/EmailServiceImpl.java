package com.example.MyNetWork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;


@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String receiver,String topic,String massage){


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nikolajveresagin665@gmail.com");
        message.setTo(receiver);
        message.setSubject(topic);
        message.setText(massage);
        javaMailSender.send(message);

    }
}