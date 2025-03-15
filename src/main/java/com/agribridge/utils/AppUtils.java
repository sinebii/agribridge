package com.agribridge.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

public class AppUtils {

    public static String findAccountUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public static String getAccountVerificationMail(String name, String token){
        return "Hello "+name+ ",\n\n Your account has been created. Please click on the link below to verify your account \n\n" +
                token + "\n\n AgriBridge Team";
    }

    public static String getAccountVerificationMail(String name, String to, String amount){
        amount = Long.valueOf(amount).toString();
        Integer rates = Integer.valueOf(amount);
        return "Hello "+name+ ",\n\n Your account has been credited with with  \n\n" + rates + "\n\n AgriBridge Team Team";
    }
}
