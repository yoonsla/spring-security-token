package com.sy.springsecuritytoken.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;

public class HeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse res = (HttpServletResponse) response;
        final String allowedMethods = String.join(
            ",",
            HttpMethod.GET.name(),
            HttpMethod.OPTIONS.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name()
        );
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", allowedMethods);
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader(
            "Access-Control-Allow-Headers",
            "X-Requested-With, Content-Type, Authorization, X-XSRF-token"
        );
        res.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }
}
