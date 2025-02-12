package com.student.app.bidworm.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String email;

    public void sendVerificationEmail(String to, String token){
        String subject ="Bidworm: Email Verification";
        String body="Please verify your email address to start using Bidworm!\n"+
                "http://localhost:8080/users/v1/verify?token="+token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
