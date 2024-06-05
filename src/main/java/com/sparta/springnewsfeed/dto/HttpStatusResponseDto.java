package com.sparta.springnewsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // 추가
@RequiredArgsConstructor
public class HttpStatusResponseDto {
    private final String code;
    private final String message;
}