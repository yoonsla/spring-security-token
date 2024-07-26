package com.sy.springsecuritytoken.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "성공"),
    FAIL("1000", "실패");

    private String code;
    private String message;
}
