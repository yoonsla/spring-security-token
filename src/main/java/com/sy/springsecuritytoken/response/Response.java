package com.sy.springsecuritytoken.response;

import lombok.Getter;

@Getter
public class Response<T> {

    public static final Response<Void> OK = new Response<>();

    private final String success = ResponseCode.SUCCESS.name();
    private final String code = ResponseCode.SUCCESS.getCode();
    private final String message = ResponseCode.SUCCESS.getMessage();
    private T result;

    private Response() {
    }

    public Response(T result) {
        this.result = result;
    }
}
