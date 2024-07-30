package com.sy.springsecuritytoken.user.service;

import com.sy.springsecuritytoken.exception.ApplicationException;
import com.sy.springsecuritytoken.response.ResponseCode;
import com.sy.springsecuritytoken.security.AccountInfo;
import com.sy.springsecuritytoken.user.repository.UserRepository;
import com.sy.springsecuritytoken.user.service.dto.SecurityUserDetailsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public SecurityUserDetailsDto loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
            .map(user -> new SecurityUserDetailsDto(
                AccountInfo.from(user),
                List.of(new SimpleGrantedAuthority(user.getRole().getValue())))
            )
            .orElseThrow(() -> new ApplicationException(ResponseCode.NOT_FOUND));
    }
}
