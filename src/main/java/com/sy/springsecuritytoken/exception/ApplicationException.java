package com.sy.springsecuritytoken.exception;

import com.sy.springsecuritytoken.response.ResponseCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ResponseCode responseCode;
    private String additionalMessage;

    public ApplicationException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ApplicationException(ResponseCode responseCode, String message) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.additionalMessage = message;
    }
}
