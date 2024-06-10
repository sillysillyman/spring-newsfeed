package com.sparta.springnewsfeed.auth;

import static com.sparta.springnewsfeed.auth.JwtUtil.BEARER_PREFIX;

import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        String uri = req.getRequestURI();
        String method = req.getMethod();
        log.info("Requested URI: {}", uri);

        //회원가입과 로그인 엔드포인트는 필터링하지 않음
        if (uri.equals("/users/signup") || uri.equals("/users/login")) {
            filterChain.doFilter(req, res);
            return;
        }

        // 이메일 인증 관련 경로는 필터링하지 않음
        if (uri.startsWith("/email-verification")) {
            filterChain.doFilter(req, res);
            return;
        }

        // 특정 엔드포인트들 필터링하지 않음
        if (method.equals("GET") && (
            uri.equals("/users/posts") ||
                uri.matches("/users/[^/]+/following/posts") ||
                uri.matches("/users/[^/]+/posts") ||
                uri.matches("/users/[^/]+/posts/[^/]+"))) {
            filterChain.doFilter(req, res);
            return;
        }

        //HTTP 요청 헤더에서 JWT 토큰 값을 가져옴. 요청헤더에서 토큰 추출
        String accessToken = jwtUtil.getAccessTokenFromHeader(req);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

        //토큰존재여부확인
        if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
            boolean accessTokenValid = jwtUtil.validateToken(accessToken);
            boolean refreshTokenValid = jwtUtil.validateToken(refreshToken);

            if (accessTokenValid && refreshTokenValid) {
                log.info("handleValidTokens");
                handleValidTokens(req, res, filterChain, accessToken, refreshToken);
            } else if (!accessTokenValid && refreshTokenValid) {
                log.info("handleExpiredAccessToken");
                handleExpiredAccessToken(req, res, filterChain, refreshToken);
            } else if (accessTokenValid && !refreshTokenValid) {
                log.info("handleExpiredRefreshToken");
                handleExpiredRefreshToken(req, res, filterChain, accessToken);
            } else {
                log.info("handleInvalidTokens");
                handleInvalidTokens(res);
            }
        } else {
            handleInvalidTokens(res);
        }
    }

    //액세스 토큰과 리프레시 토큰이 모두 유효한 경우 인증을 설정하고 요청을 필터링.
    private void handleValidTokens(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain, String accessToken, String refreshToken)
        throws IOException, ServletException {
        // 액세스 토큰에서 클레임(사용자 정보)을 추출
        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
        String userId = accessTokenClaims.getSubject();

        // 데이터베이스에서 사용자 정보 조회
        User user = userRepository.findByUserId(userId).orElse(null);
        log.info("user ID: {}", user.getUserId());
        log.info("user token: {}", user.getRefreshToken().substring(BEARER_PREFIX.length()));
        log.info("refreshToken: {}", refreshToken);

        // 사용자가 존재하고, 요청의 리프레시 토큰이 DB에 저장된 리프레시 토큰과 일치하는지 확인
        if (user != null && refreshToken.equals(
            user.getRefreshToken().substring(BEARER_PREFIX.length()))) {
            // 사용자 인증 설정
            setAuthentication(userId);
            // 요청 필터링 수행
            filterChain.doFilter(req, res);
        } else {
            // 토큰이 유효하지 않은 경우 처리
            handleInvalidTokens(res);
        }
    }

    //액세스 토큰이 만료되고 리프레시 토큰이 유효한 경우 새로운 액세스 토큰을 생성하고 응답 헤더에 추가.
    private void handleExpiredAccessToken(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain, String refreshToken) throws IOException, ServletException {
        Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
        String userId = refreshTokenClaims.getSubject();

        User user = userRepository.findByUserId(userId).orElse(null);
        if (user != null && refreshToken.equals(user.getRefreshToken())) {
            String newAccessToken = jwtUtil.createAccessToken(userId);
            res.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
            setAuthentication(userId);
            filterChain.doFilter(req, res);
        } else {
            handleInvalidTokens(res);
        }
    }

    //액세스 토큰이 유효하고 리프레시 토큰이 만료된 경우 새로운 리프레시 토큰을 생성하고 응답 헤더에 추가.
    private void handleExpiredRefreshToken(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain, String accessToken) throws IOException, ServletException {
        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
        String userId = accessTokenClaims.getSubject();

        User user = userRepository.findByUserId(userId).orElse(null);
        if (user != null) {
            String newRefreshToken = jwtUtil.createRefreshToken(userId);
            res.addHeader("Refresh-Token", newRefreshToken);
            saveRefreshTokenToDatabase(userId, newRefreshToken);
            setAuthentication(userId);
            filterChain.doFilter(req, res);
        } else {
            handleInvalidTokens(res);
        }
    }

    private void handleInvalidTokens(HttpServletResponse res) throws IOException {
        log.error("Invalid Tokens");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


    // Refresh Token을 DB에 저장하는 메서드
    private void saveRefreshTokenToDatabase(String userId, String newRefreshToken) {
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user != null) {
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);
        } else {
            log.error("User not found: " + userId);
        }
    }


    // 인증 처리-사용자 아이디를 기반으로 Spring Security의 인증 객체를 생성하고, 인증 컨텍스트에 설정
    public void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}
