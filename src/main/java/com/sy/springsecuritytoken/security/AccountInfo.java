package com.sy.springsecuritytoken.security;

import com.sy.springsecuritytoken.user.domain.Account;
import com.sy.springsecuritytoken.user.domain.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountInfo {

    private Long seq;

    private String email;

    private String password;

    private UserRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static AccountInfo from(Account user) {
        return new AccountInfo(
            user.getSeq(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    public static AccountInfo parse(String email) {
        return new AccountInfo(null, email, null, null, null, null);
    }
}
