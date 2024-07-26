package com.sy.springsecuritytoken.error.controller.response;

import com.sy.springsecuritytoken.response.ResponseCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String error;
    private final String code;
    private final String message;

    private ErrorResponse(ResponseCode errorCode) {
        this.error = errorCode.name();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse of(ResponseCode errorCode) {
        return new ErrorResponse(errorCode);
    }
}
