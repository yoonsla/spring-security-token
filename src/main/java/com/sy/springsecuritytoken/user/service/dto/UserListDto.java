package com.sy.springsecuritytoken.user.service.dto;

import com.sy.springsecuritytoken.user.domain.Account;
import com.sy.springsecuritytoken.user.domain.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserListDto {

    private Long seq;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserListDto from(Account account) {
        return new UserListDto(
            account.getSeq(),
            account.getEmail(),
            account.getRole(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}
