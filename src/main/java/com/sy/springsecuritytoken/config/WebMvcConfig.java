package com.sy.springsecuritytoken.config;

import com.sy.springsecuritytoken.security.interceptor.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
            .addPathPatterns("/**") // 모든 URL에 대해 JWT 토큰 검사
            .excludePathPatterns("/user/login", "/login"); // 로그인 페이지는 JWT 토큰 검사에서 제외
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/static/",
        "classpath:/public/",
        "classpath:/",
        "classpath:/resources/",
        "classpath:/META-INF/resources/",
        "classpath:/META-INF/resources/webjars/"
    };

    /*
        뷰 컨트롤러를 추가할때 사용, 루트 URL("/")에 접근했을 때 "/login"으로 리다이렉트하도록 설정
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /*
        정적 리소스를 처리하는 핸들러를 추가하는데 사용된다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
