package com.solo.delivery.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String toAddress, String content) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toAddress);
        mail.setFrom("${spring.mail.username}");
        mail.setSubject("[MyDeliveryApp] Server Error 발생");
        mail.setText(content);
        javaMailSender.send(mail);

    }

}
