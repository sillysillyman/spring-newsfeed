package com.sparta.springnewsfeed.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.LoginRequestDto;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.auth.WithdrawRequestDto;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil,UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository=userRepository;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<HttpStatusResponseDto> signup(@Validated @RequestBody SignupRequestDto requestDto) {
        ResponseCode responseCode = userService.signup(requestDto);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<HttpStatusResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            ResponseCode responseCode = userService.login(requestDto);
            HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
            return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
        } catch (IllegalArgumentException e) {
            return handleIllegalArgumentException(e);
        }
    }

    // 프로필 수정
    @PutMapping("/{userId}/update-profile")
    public ResponseEntity<HttpStatusResponseDto> updateProfile(
            @PathVariable String userId,
            @Validated @RequestPart("updateProfileRequestDto") UpdateProfileRequestDto requestDto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        ResponseCode responseCode = userService.updateProfile(requestDto, profilePicture, userId);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 바밀번호 변경
    @PutMapping("/{userId}/update-password")
    public ResponseEntity<HttpStatusResponseDto> updatePassword(
            @PathVariable String userId,
            @Validated @RequestBody UpdatePasswordRequestDto requestDto) {
        ResponseCode responseCode = userService.updatePassword(requestDto, userId);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 사용자 프로필 조회
    @GetMapping("/{userId}/inquiry")
    public ResponseEntity<UserInquiryResponseDto> getUserProfile(@PathVariable String userId) {
        UserInquiryResponseDto userProfile = userService.getUserProfile(userId);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatusResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 일반 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpStatusResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseCode responseCode;
        if (ex.getMessage().equals(ResponseCode.DUPLICATE_ENTITY.getMessage())) {
            responseCode = ResponseCode.DUPLICATE_ENTITY;
        } else if (ex.getMessage().equals(ResponseCode.INVALID_INPUT_VALUE.getMessage())) {
            responseCode = ResponseCode.INVALID_INPUT_VALUE;
        } else {
            responseCode = ResponseCode.INTERNAL_SERVER_ERROR;
        }
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatusResponseDto> handleException(Exception ex) {
        ex.printStackTrace();
        HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 헤더에서 토큰을 가져옴 ->
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        // 토큰이 유효한지 확인
        if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
            String userId = refreshTokenClaims.getSubject();

            // 유저의 리프레시 토큰 삭제
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                user.setRefreshToken(null);
                userRepository.save(user);
            }
        }

        // 클라이언트 측 토큰 삭제 요청을 위해 응답 설정
        return ResponseEntity.ok("Logged out successfully");
    }



    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(HttpServletRequest request, @RequestBody WithdrawRequestDto requestDto) {

        // 헤더에서 토큰을 가져옴
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        // 토큰이 유효한지 확인 (여기서는 accessToken을 사용하여 유효성 검증)
        if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
            String userId = accessTokenClaims.getSubject();

            // 유저 정보 가져오기
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // 비밀번호가 일치하는지 확인
            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }

            // 리프레시 토큰도 유효한지 확인
            if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                // 유저의 리프레시 토큰 삭제, 유저의 상태변경
                user.setRefreshToken(null);
                user.setStatus(UserStatusEnum.DELETED);
                userRepository.save(user);

                // 클라이언트 측 토큰 삭제 요청을 위해 응답 설정
                return ResponseEntity.ok("User deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
        }
    }

    }


