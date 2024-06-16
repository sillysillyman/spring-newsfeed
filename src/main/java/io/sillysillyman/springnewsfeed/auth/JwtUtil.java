package io.sillysillyman.springnewsfeed.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    //private final long ACCESS_TOKEN_TIME=1000L;
    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //ACCESS_TOKEN생성
    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_TIME);
    }

    //REFRESH_TOKEN생성
    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_TIME);
    }

    //토큰생성
    private String createToken(String username, long expirationTime) {
        Date date = new Date();

        JwtBuilder builder = Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(date.getTime() + expirationTime))//만료시간
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm);

        return BEARER_PREFIX + builder.compact();
    }

    // Access Token 헤더에서 JWT 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    // Refresh Token 헤더에서 JWT 가져오기
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    // JWT 토큰에서 username 추출-post에서사용
    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
