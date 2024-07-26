package com.sy.springsecuritytoken.user.service;

import com.sy.springsecuritytoken.exception.NotFountException;
import com.sy.springsecuritytoken.user.domain.AccountDetail;
import com.sy.springsecuritytoken.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AccountDetail loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
            .map(user -> new AccountDetail(user, List.of(new SimpleGrantedAuthority(user.getRole().getValue()))))
            .orElseThrow(() -> new NotFountException(email + ": load failed"));
    }
}
