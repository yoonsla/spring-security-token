package com.sy.springsecuritytoken.interceptor;

import com.sy.springsecuritytoken.annotation.AnonymousCallable;
import com.sy.springsecuritytoken.constants.AuthConstants;
import com.sy.springsecuritytoken.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        if (HttpMethod.OPTIONS.matches(httpRequest.getMethod())) {
            return true;
        }
        final Method method = handlerMethod.getMethod();
        if (isAnonymousCallable(method)) {
            return true;
        }
        final String accessToken = httpRequest.getHeader(AuthConstants.AUTH_HEADER);
        if (accessToken != null) {
            TokenUtil.validToken(accessToken);
            return true;
        }
        httpResponse.sendRedirect("/error/unauthorized");
        return false;
    }

    private boolean isAnonymousCallable(Method method) {
        final AnonymousCallable anonymousCallable = method.getDeclaredAnnotation(AnonymousCallable.class);
        return anonymousCallable != null;
    }
}
