package com.agribridge.service;

public interface EmailService {

    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachment(String name, String to, String token);
    void sendMimeMessageWithEmbeddedImage(String name, String to, String token);
    void sendHtmlEmail(String name, String to, String token);
    void sendPaymentNotification(String name, String to, String amount);
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
