package com.app.springbootteamprolearningplatform.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class Util {

    public static boolean isValidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }
}
