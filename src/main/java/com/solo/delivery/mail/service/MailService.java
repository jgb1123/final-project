package com.solo.delivery.mail.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String mailUsername;

    private final JavaMailSender javaMailSender;

    public void sendEmail(String content) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailUsername);
        mail.setFrom(mailUsername);
        mail.setSubject("[MyDeliveryApp] Server Error 발생");
        mail.setText(content);
        javaMailSender.send(mail);
    }
}
