package com.agribridge.events.listener;

import com.agribridge.events.RegistrationCompleteEvent;
import com.agribridge.model.User;
import com.agribridge.service.AuthenticationService;
import com.agribridge.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public RegistrationCompleteEventListener(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        authenticationService.createVerificationToken(user, verificationToken);
        String appUrl = event.getAppUrl()+"/api/v1/auth/verify-account?token="+verificationToken;
//        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), appUrl);
//        emailService.sendMimeMessageWithAttachment(user.getName(), user.getEmail(), appUrl);
        emailService.sendHtmlEmail(user.getFirstName() + " "+user.getLastName(), user.getEmail(), appUrl);
        log.info("Registration complete event for user: "+appUrl);
    }
}
