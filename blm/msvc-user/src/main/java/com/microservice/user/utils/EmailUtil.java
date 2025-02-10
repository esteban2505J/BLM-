package com.microservice.user.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

@AllArgsConstructor
@Service
public class EmailUtil {

    final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text){
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

        }catch (MessagingException e) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        }
    }

}
