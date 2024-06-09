package com.sparta.springnewsfeed.common;

import lombok.Getter;

@Getter
public class HttpStatusResponseDto {

    private final int statusCode;
    private final String code;
    private final String message;
    private final Object data;

    public HttpStatusResponseDto(ResponseCode responseCode) {
        this.statusCode = responseCode.getStatusCode();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = null;
    }

    public HttpStatusResponseDto(ResponseCode responseCode, Object data) {
        this.statusCode = responseCode.getStatusCode();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }
}