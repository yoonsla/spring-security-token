package com.sy.springsecuritytoken.security.provider;

import com.sy.springsecuritytoken.user.service.dto.SecurityUserDetailsDto;
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
        final SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) userDetailsService.loadUserByUsername(email);
        if (passwordEncoder.matches(password, securityUserDetailsDto.getPassword())) {
            return new UsernamePasswordAuthenticationToken(securityUserDetailsDto, password, securityUserDetailsDto.getAuthorities());
        }
        throw new BadCredentialsException(securityUserDetailsDto.getUsername() + "Invalid password");
    }

    /*
        AuthenticationProvider가 특정 Authentication 타입을 지원하는지 여부를 반환, UsernamePasswordAuthenticationToken 클래스를 지원한다고 명시
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
