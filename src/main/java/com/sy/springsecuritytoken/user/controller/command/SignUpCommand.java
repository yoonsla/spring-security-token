package com.sy.springsecuritytoken.user.controller.command;

import com.sy.springsecuritytoken.user.domain.UserRole;
import lombok.Getter;

@Getter
public class SignUpCommand {

    private String email;
    private String password;
    private UserRole userRole;
}
