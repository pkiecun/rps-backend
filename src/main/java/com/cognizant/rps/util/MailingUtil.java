package com.cognizant.rps.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailingUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String toEmail,
                         String subject,
                         String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testUser15379@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");

    }

}