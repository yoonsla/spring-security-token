package com.sy.springsecuritytoken.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.springsecuritytoken.exception.AuthenticationProcessingException;
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

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            final Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            authRequest = new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword());
        } catch (IOException e) {
            throw new AuthenticationProcessingException("Failed ==> attemptAuthentication");
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
