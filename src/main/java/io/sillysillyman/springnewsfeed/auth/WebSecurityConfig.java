package io.sillysillyman.springnewsfeed.auth;

import io.sillysillyman.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil,
            userRepository);
        jwtAuthenticationFilter.setAuthenticationManager(
            authenticationManager(authenticationConfiguration));
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // CSRF 설정
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        httpSecurity.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll() // resources 접근 허용 설정
                .requestMatchers("/", "/error").permitAll() // 메인 페이지 및 에러 페이지 요청 허가
                .requestMatchers("/users/signup", "/users/login").permitAll() // 회원가입 및 로그인 요청 허가
                .requestMatchers("/email-verification/**").permitAll() // 이메일 인증 관련 요청 허가
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        // httpSecurity.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthorizationFilter(),
            UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
