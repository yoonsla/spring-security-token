package com.sy.springsecuritytoken.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "성공"),
    UNMATCHED_USER_INFO("1000", "로그인 정보가 일치하지 않습니다."),
    LOCKED("1001", "계정이 잠겨 있습니다."),
    UNAVAILABLE("1002", "계정이 비활성화되었습니다."),
    EXPIRED_USER("1003", "계정이 만료되었습니다."),
    EXPIRED_TOKEN("1004", "인증 정보가 만료되었습니다."),
    NO_ACCESS_TOKEN("1005", "토큰이 존재하지 않습니다."),
    INVALID_AUTHENTICATION("1006", "잘못된 인증 정보 입니다."),
    ALREADY_EXIST("2000", "이미 존재합니다."),
    NOT_FOUND("2001", "존재하지 않습니다."),
    BAD_REQUEST("2002", "잘못된 요청입니다."),
    UNKNOWN("2000", "알 수 없는 에러가 발생햇습니다.");

    private String code;
    private String message;
}
