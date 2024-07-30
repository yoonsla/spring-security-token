package com.sy.springsecuritytoken.security.filter;

import com.sy.springsecuritytoken.constants.AuthConstants;
import com.sy.springsecuritytoken.security.AccountInfo;
import com.sy.springsecuritytoken.user.service.dto.SecurityUserDetailsDto;
import com.sy.springsecuritytoken.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
        throws ServletException, IOException {

        List<String> list = Arrays.asList(
            "/user/login",
            "/login",
            "/css/**",
            "/js/**",
            "/images/**"
        );

        // 토큰이 필요하지 않은 API URL의 경우 -> 로직 처리없이 다음 필터로 이동
        if (list.contains(httpRequest.getRequestURI())) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        // OPTIONS 요청일 경우 -> 로직 처리 없이 다음 필터로 이동
        if (HttpMethod.OPTIONS.matches(httpRequest.getMethod())) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        final String accessToken = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        if (accessToken != null) {
            final AccountInfo accountInfo = TokenUtil.parseToken(accessToken);
            final SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) userDetailsService.loadUserByUsername(accountInfo.getEmail());
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                securityUserDetailsDto,
                null,
                securityUserDetailsDto.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }
}
