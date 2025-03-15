package com.agribridge.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageUtil {
    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public String getMessage(String code, String locale) {
        return messageSource.getMessage(code, null, new Locale(locale));
    }
}
