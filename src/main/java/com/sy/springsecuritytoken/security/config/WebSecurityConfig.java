package com.sy.springsecuritytoken.security.config;

import com.sy.springsecuritytoken.security.filter.CustomAuthenticationFilter;
import com.sy.springsecuritytoken.security.filter.JwtAuthorizationFilter;
import com.sy.springsecuritytoken.security.handler.CustomLoginFailHandler;
import com.sy.springsecuritytoken.security.handler.CustomLoginSuccessHandler;
import com.sy.springsecuritytoken.security.provider.CustomAuthenticationProvider;
import com.sy.springsecuritytoken.user.service.CustomUserDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        CustomAuthenticationFilter customAuthenticationFilter,
        JwtAuthorizationFilter jwtAuthorizationFilter
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/resources/**").permitAll()
                .requestMatchers("/main/rootPage").permitAll()// 자원에 대한 요청은 가능, 그 외에는 인증 필요
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/main/rootPage"))
                .permitAll()
            )
            .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(
        AuthenticationManager authenticationManager,
        CustomLoginSuccessHandler customLoginSuccessHandler,
        CustomLoginFailHandler customLoginFailHandler
    ) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        // "/user/login" 엔드포인트로 들어오는 요청을 CustomAuthenticationFilter에서 처리하도록 지정한다.
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler);
        // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailHandler);
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public CustomLoginFailHandler customLoginFailureHandler() {
        return new CustomLoginFailHandler();
    }

    /*
       JWT 인증 필터 생성
       JWT 인증 필터는 요청 헤더의 JWT 토큰을 검증하고 토큰이 유효하면 토큰에서 사용자의 정보와 권한을 추출하여 SecurityContext에 저장
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(CustomUserDetailService userDetailsService) {
        return new JwtAuthorizationFilter(userDetailsService);
    }
}
