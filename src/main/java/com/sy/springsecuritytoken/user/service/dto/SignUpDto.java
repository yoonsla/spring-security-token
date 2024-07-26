package com.sy.springsecuritytoken.user.service.dto;

import com.sy.springsecuritytoken.user.domain.Account;
import com.sy.springsecuritytoken.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpDto {

    private String email;
    private UserRole role;

    public static SignUpDto from(Account account) {
        return new SignUpDto(account.getEmail(), account.getRole());
    }
}
