package com.sparta.springnewsfeed.user;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatusResponseDto> signup(@Validated @RequestBody SignupRequestDto requestDto) {
        ResponseCode responseCode = userService.signup(requestDto);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
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
}
