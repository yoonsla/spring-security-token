package com.sy.springsecuritytoken.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationProcessingException extends AuthenticationException {

    public AuthenticationProcessingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AuthenticationProcessingException(String message) {
        super(message);
    }
}
