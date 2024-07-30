package com.sy.springsecuritytoken.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.springsecuritytoken.exception.AuthenticationProcessingException;
import com.sy.springsecuritytoken.response.ResponseCode;
import com.sy.springsecuritytoken.user.domain.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /*
        - AuthenticationManager 객체를 인자로 받아 부모 클래스의 생성자에 전달
        - AuthenticationManager는 인증을 처리하는 데 사용됨
     */
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    /*
        1. 사용자가 로그인을 시도할 때 호출
        2. HTTP 요청에서 사용자 이름과 비밀번호를 추출하여 UsernamePasswordAuthenticationToken 객체를 생성하고, 이를 AuthenticationManager에 전달하여 인증 시도
        3. 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 Authentication 객체를 반환하고, 인증이 실패하면 Exception을 던진다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            final Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            authRequest = new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword());
        } catch (IOException e) {
            log.error("인증 실패");
            throw new AuthenticationProcessingException(ResponseCode.INVALID_AUTHENTICATION);
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
