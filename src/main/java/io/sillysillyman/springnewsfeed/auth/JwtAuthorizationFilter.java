package io.sillysillyman.springnewsfeed.auth;

import static io.sillysillyman.springnewsfeed.auth.JwtUtil.BEARER_PREFIX;

import io.jsonwebtoken.Claims;
import io.sillysillyman.springnewsfeed.user.User;
import io.sillysillyman.springnewsfeed.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
        UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("Requested URI: {}", uri);

        //회원가입과 로그인 엔드포인트는 필터링하지 않음
        if (uri.equals("/users/signup") || uri.equals("/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이메일 인증 관련 경로는 필터링하지 않음
        if (uri.startsWith("/email-verification")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 특정 엔드포인트들 필터링하지 않음
        if (method.equals("GET") && (uri.matches("/[^/]+") ||
            uri.matches("/[^/]+/[^/]+") || uri.matches("/[^/]+/following")) || uri.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        //HTTP 요청 헤더에서 JWT 토큰 값을 가져옴. 요청헤더에서 토큰 추출
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        //토큰존재여부확인
        if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
            boolean isAccessTokenValid = jwtUtil.validateToken(accessToken);
            boolean isRefreshTokenValid = jwtUtil.validateToken(refreshToken);

            if (isAccessTokenValid && isRefreshTokenValid) {
                log.info("handleValidTokens");
                handleValidTokens(request, response, filterChain, accessToken, refreshToken);
            } else if (!isAccessTokenValid && isRefreshTokenValid) {
                log.info("handleExpiredAccessToken");
                handleExpiredAccessToken(request, response, filterChain, refreshToken);
            } else if (isAccessTokenValid) {
                log.info("handleExpiredRefreshToken");
                handleExpiredRefreshToken(request, response, filterChain, accessToken);
            } else {
                log.info("handleInvalidTokens");
                handleInvalidTokens(response);
            }
        } else {
            handleInvalidTokens(response);
        }
    }

    //액세스 토큰과 리프레시 토큰이 모두 유효한 경우 인증을 설정하고 요청을 필터링.
    private void handleValidTokens(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, String accessToken, String refreshToken)
        throws IOException, ServletException {
        // 액세스 토큰에서 클레임(사용자 정보)을 추출
        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
        String username = accessTokenClaims.getSubject();

        // 데이터베이스에서 사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.info("username: {}", user.getUsername());
            log.info("user token: {}", user.getRefreshToken().substring(BEARER_PREFIX.length()));
            log.info("refreshToken: {}", refreshToken);

            if (refreshToken.equals(user.getRefreshToken().substring(BEARER_PREFIX.length()))) {
                // 사용자 인증 설정
                setAuthentication(username);
                // 요청 필터링 수행
                filterChain.doFilter(request, response);
                return;
            }
        }
        handleInvalidTokens(response);
    }

    //액세스 토큰이 만료되고 리프레시 토큰이 유효한 경우 새로운 액세스 토큰을 생성하고 응답 헤더에 추가.
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, String refreshToken) throws IOException, ServletException {
        Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = refreshTokenClaims.getSubject();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (refreshToken.equals(user.getRefreshToken())) {
                String newAccessToken = jwtUtil.createAccessToken(username);

                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                setAuthentication(username);
                filterChain.doFilter(request, response);
                return;
            }
        }
        handleInvalidTokens(response);
    }

    //액세스 토큰이 유효하고 리프레시 토큰이 만료된 경우 새로운 리프레시 토큰을 생성하고 응답 헤더에 추가.
    private void handleExpiredRefreshToken(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, String accessToken) throws IOException, ServletException {
        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
        String username = accessTokenClaims.getSubject();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            String newRefreshToken = jwtUtil.createRefreshToken(username);

            response.addHeader("Refresh-Token", newRefreshToken);
            saveRefreshTokenToDatabase(username, newRefreshToken);
            setAuthentication(username);
            filterChain.doFilter(request, response);
            return;
        }
        handleInvalidTokens(response);
    }

    private void handleInvalidTokens(HttpServletResponse response) {
        log.error("Invalid Tokens");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


    // Refresh Token을 DB에 저장하는 메서드
    private void saveRefreshTokenToDatabase(String username, String newRefreshToken) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);
            return;
        }
        log.error("User not found: " + username);
    }


    // 인증 처리-사용자 아이디를 기반으로 Spring Security의 인증 객체를 생성하고, 인증 컨텍스트에 설정
    public void setAuthentication(String username) {
        SecurityContextHolder.getContext().setAuthentication(createAuthentication(username));
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}
