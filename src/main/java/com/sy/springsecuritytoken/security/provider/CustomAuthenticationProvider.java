package com.sy.springsecuritytoken.security.provider;

import com.sy.springsecuritytoken.user.domain.AccountDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // AuthenticationFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        final String email = token.getName();
        final String password = (String) token.getPrincipal();
        final AccountDetail accountDetail = (AccountDetail) userDetailsService.loadUserByUsername(email);
        if (passwordEncoder.matches(password, accountDetail.getPassword())) {
            return new UsernamePasswordAuthenticationToken(accountDetail, password, accountDetail.getAuthorities());
        }
        throw new BadCredentialsException(accountDetail.getUsername() + "Invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
