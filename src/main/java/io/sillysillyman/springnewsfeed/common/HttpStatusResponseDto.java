package io.sillysillyman.springnewsfeed.common;

import lombok.Getter;

@Getter
public class HttpStatusResponseDto<T> {

    private final ResponseCode responseCode;
    private final T data;

    public HttpStatusResponseDto(ResponseCode responseCode) {
        this.responseCode = responseCode;
        this.data = null;
    }

    public HttpStatusResponseDto(ResponseCode responseCode, T data) {
        this.responseCode = responseCode;
        this.data = data;
    }
}