package com.sparta.springnewsfeed.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import com.sparta.springnewsfeed.user.UserStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationAuthorizationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // 테스트전 사용자 생성
        User user = new User();
        user.setUserId("testuserid");
        user.setName("TestUser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user);
    }

    @AfterEach
    public void cleanup() {//테스트후 실행. 사용자 저장소 정리해 데스트데이터가 다른테스트에 영향 미치지않게 함.
        userRepository.deleteAll();
    }

    //유효한 사용자 정보로 로그인 요청을 보내고, 성공적인 로그인 응답과 함께 JWT Access Token과 Refresh Token이 헤더에 포함되어 있는지 확인
    @Test
    public void testSuccessfulLogin() throws Exception {
        // 로그인 요청
        LoginRequestDto loginRequestDto = new LoginRequestDto("testuserid", "password");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists(JwtUtil.AUTHORIZATION_HEADER))
                .andExpect(header().exists("Refresh-Token"));
    }

    //상태가 UNVERIFIED인 사용자로 로그인 요청을 보내고, 인증되지 않은 상태임을 나타내는 응답을 받는지 확인
    // => Expected :인증이 필요합니다.
    //      Actual :
    @Test
    public void testLoginUnverifiedUser() throws Exception {
        // 인증되지 않은 사용자 생성
        User user = new User();
        user.setUserId("unverifiedUser");
        user.setName("unverifiedTestUser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setStatus(UserStatusEnum.UNVERIFIED);
        userRepository.save(user);

        // 로그인 요청
        LoginRequestDto loginRequestDto = new LoginRequestDto("unverifiedUser", "password");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("인증이 필요합니다."));
    }

    //상태가 DELETED인 사용자로 로그인 요청을 보내고, 탈퇴된 사용자임을 나타내는 응답을 받는지 확인-could not execute statement [Column 'name' cannot be null 발생함.->404예상->401발생(인증되지않은 클라이언트라는 의미. 이거도 상관없긴하지 않나? 없는 사용자나 마찬가지니까?)
    @Test
    public void testLoginDeletedUser() throws Exception {
        // 삭제된 사용자 생성
        User user = new User();
        user.setUserId("deletedUser");
        user.setName("deletedTestUser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setStatus(UserStatusEnum.DELETED);
        userRepository.save(user);

        // 로그인 요청
        LoginRequestDto loginRequestDto = new LoginRequestDto("deletedUser", "password");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("탈퇴한 사용자입니다."));
    }

    //유효한 Access Token을 사용하여 보호된 엔드포인트에 접근할 때, 접근이 허용되는지 확인-200을 예상했는데404발생..
    @Test
    public void testAuthorizationWithValidAccessToken() throws Exception {
        // 유효한 액세스 토큰 생성
        String accessToken = jwtUtil.createAccessToken("testuserid");

        // 보호된 엔드포인트 요청
        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, accessToken))
                .andExpect(status().isOk());
    }

    //유효한 Access Token을 사용하여 보호된 엔드포인트에 접근할 때 성공적으로 응답을 받는지 확인
    @Test
    public void testValidAccessToken() throws Exception {
        String accessToken = jwtUtil.createAccessToken("testuserid");

        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    //만료된 Access Token과 유효한 Refresh Token을 사용하여 접근할 때 새로운 Access Token이 재발급되는지 확인
    @Test
    public void testExpiredAccessTokenAndValidRefreshToken() throws Exception {
        String accessToken = jwtUtil.createAccessToken("testuserid"); // Expired access token
        String refreshToken = jwtUtil.createRefreshToken("testuserid");

        // Save refresh token in the database
        User user = userRepository.findByUserid("testuserid").orElse(null);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, "Bearer " + accessToken)
                        .header("Refresh-Token", refreshToken))
                .andExpect(status().isOk())
                .andExpect(header().exists(JwtUtil.AUTHORIZATION_HEADER)); // Check if new access token is issued
    }

    //
    @Test
    public void testExpiredAccessTokenAndInvalidRefreshToken() throws Exception {
        String accessToken = jwtUtil.createAccessToken("testuserid"); // Expired access token
        String invalidRefreshToken = "invalidRefreshToken";

        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, "Bearer " + accessToken)
                        .header("Refresh-Token", invalidRefreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testBothTokensInvalid() throws Exception {
        String invalidAccessToken = "invalidAccessToken";
        String invalidRefreshToken = "invalidRefreshToken";

        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, "Bearer " + invalidAccessToken)
                        .header("Refresh-Token", invalidRefreshToken))
                .andExpect(status().isUnauthorized());
    }


    //유효하지 않은 Access Token을 사용하여 보호된 엔드포인트에 접근할 때, 접근이 거부되는지 확인(0)
    @Test
    public void testAuthorizationWithInvalidAccessToken() throws Exception {
        // 유효하지 않은 액세스 토큰 사용
        String invalidAccessToken = "invalidToken";

        // 보호된 엔드포인트 요청
        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, invalidAccessToken))
                .andExpect(status().isUnauthorized());
    }

    //만료된 Access Token을 사용하여 보호된 엔드포인트에 접근할 때, 접근이 거부되는지 확인(0)
    @Test
    public void testAuthorizationWithExpiredAccessToken() throws Exception {
        // 만료된 액세스 토큰 생성 (만료 시간을 1초로 설정하여 테스트)
        String expiredAccessToken = jwtUtil.createAccessToken("testuser");

        // 토큰 만료 대기
        Thread.sleep(2000);

        // 보호된 엔드포인트 요청
        mockMvc.perform(get("/protected/endpoint")
                        .header(JwtUtil.AUTHORIZATION_HEADER, expiredAccessToken))
                .andExpect(status().isUnauthorized());
    }
}
