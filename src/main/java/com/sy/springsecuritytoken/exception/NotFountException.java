package com.sy.springsecuritytoken.exception;

public class NotFountException extends RuntimeException {

    public NotFountException(String email) {
        super(email + " NotFoundException");
    }
}
