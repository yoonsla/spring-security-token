package com.sy.springsecuritytoken.user.controller.response;

import com.sy.springsecuritytoken.user.domain.UserRole;
import com.sy.springsecuritytoken.user.service.dto.UserListDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserListResponse {

    private Long seq;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserListResponse from(UserListDto dto) {
        return new UserListResponse(
            dto.getSeq(),
            dto.getEmail(),
            dto.getRole(),
            dto.getCreatedAt(),
            dto.getUpdatedAt()
        );
    }
}
