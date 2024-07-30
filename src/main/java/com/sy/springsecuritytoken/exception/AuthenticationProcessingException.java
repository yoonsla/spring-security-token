package com.sy.springsecuritytoken.exception;

import com.sy.springsecuritytoken.response.ResponseCode;
import lombok.Getter;

@Getter
public class AuthenticationProcessingException extends RuntimeException {

    private ResponseCode responseCode = ResponseCode.INVALID_AUTHENTICATION;

    public AuthenticationProcessingException() {
        super(ResponseCode.INVALID_AUTHENTICATION.getMessage());
    }

    public AuthenticationProcessingException(Throwable cause) {
        super(ResponseCode.INVALID_AUTHENTICATION.getMessage(), cause);
    }

    public AuthenticationProcessingException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
