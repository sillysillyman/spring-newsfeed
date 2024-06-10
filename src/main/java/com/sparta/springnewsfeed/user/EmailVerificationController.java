package com.sparta.springnewsfeed.user;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email-verification")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Autowired
    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    // 이메일 인증 코드를 전송
    @PostMapping("/send")
    public ResponseEntity<HttpStatusResponseDto> sendVerificationCode(@RequestParam String email) {
        try {
            emailVerificationService.createVerificationCode(email);
            HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 이메일 인증 코드를 검증
    @PostMapping("/verify")
    public ResponseEntity<HttpStatusResponseDto> verifyCode(@Validated @RequestBody EmailVerificationRequestDto requestDto) {
        try {
            ResponseCode responseCode = emailVerificationService.verifyEmail(requestDto.getCode());
            HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
            return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
        } catch (IllegalArgumentException e) {
            HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // 이메일 인증 코드를 재발송
    @PostMapping("/resend")
    public ResponseEntity<HttpStatusResponseDto> resendVerificationCode(@RequestParam String email) {
        try {
            emailVerificationService.createVerificationCode(email);
            HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
