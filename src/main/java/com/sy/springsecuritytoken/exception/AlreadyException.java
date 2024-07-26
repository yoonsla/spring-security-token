package com.sy.springsecuritytoken.exception;

public class AlreadyException extends RuntimeException {

    public AlreadyException(String email) {
        super(email + "Is Already Exist");
    }
}
