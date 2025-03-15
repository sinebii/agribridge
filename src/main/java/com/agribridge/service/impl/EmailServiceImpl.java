package com.agribridge.service.impl;
import com.agribridge.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

import static com.agribridge.utils.AppUtils.getAccountVerificationMail;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Account Verification From AgriBridge");
            message.setFrom("NoReply@panteka.com");
            message.setTo(to);
            message.setText(getAccountVerificationMail(name, token));
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    @Async
    public void sendMimeMessageWithAttachment(String name, String to, String token) {

        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setPriority(1);
            helper.setSubject("Account Verification From AgriBridge");
            helper.setFrom("NoReply@panteka.com");
            helper.setTo(to);
            message.setText(getAccountVerificationMail(name, token));
//            FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.dir")+"/src/main/resources/static/images/F5Hqd0VWAAAYQi7.jpg"));
//            helper.addAttachment(fort.getFilename(), fort);
            String attachmentFilePath = System.getProperty("user.dir") + "/src/main/resources/static/images/F5Hqd0VWAAAYQi7.jpg";
            File attachmentFile = new File(attachmentFilePath);
            if (attachmentFile.exists()) {
                System.err.println("Attachment exist: " + attachmentFilePath);
                FileSystemResource fort = new FileSystemResource(attachmentFile);
                helper.addAttachment(fort.getFilename(), fort);
            } else {
                System.err.println("Attachment file does not exist: " + attachmentFilePath);
            }
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    @Async
    public void sendMimeMessageWithEmbeddedImage(String name, String to, String token) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setPriority(1);
            helper.setSubject("Account Verification From AgriBridge");
            helper.setFrom("NoReply@panteka.com");
            helper.setTo(to);
            message.setText(getAccountVerificationMail(name, token));
            FileSystemResource fort = new FileSystemResource(new File(System.getProperty("user.home")+"/src/main/resources/static/images/F5Hqd0VWAAAYQi7.jpg"));
            helper.addInline("<"+fort.getFilename()+">", fort);
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("token", token);
            String text = templateEngine.process("verify-account", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setPriority(1);
            helper.setSubject("Account Verification From AgriBridge");
            helper.setFrom("NoReply@panteka.com");
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPaymentNotification(String name, String to, String amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Payment Notification From AgriBridge");
            message.setFrom("sinebi.innazo@gmail.com");
            message.setTo(to);
            message.setText(getAccountVerificationMail(name,to,amount));
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {

    }

    private MimeMessage getMimeMessage() {
        return javaMailSender.createMimeMessage();
    }
}
