package com.sy.springsecuritytoken.security.handler;

import com.sy.springsecuritytoken.constants.AuthConstants;
import com.sy.springsecuritytoken.security.AccountInfo;
import com.sy.springsecuritytoken.user.service.dto.SecurityUserDetailsDto;
import com.sy.springsecuritytoken.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) authentication.getPrincipal();
        final AccountInfo account = securityUserDetailsDto.getAccount();
        final String token = TokenUtil.generateJwtToken(account);
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
    }
}
