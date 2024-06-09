package com.sparta.springnewsfeed.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "S001", "요청이 성공했습니다."),
    CREATED(201, "S002", "리소스가 성공적으로 생성되었습니다."),
    INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 입력 값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않는 메서드입니다."),
    ENTITY_NOT_FOUND(404, "C003", "엔티티를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "내부 서버 오류입니다."),
    INVALID_TYPE_VALUE(400, "C005", "유효하지 않은 타입 값입니다."),
    ACCESS_DENIED(403, "C006", "접근이 거부되었습니다."),
    UNAUTHORIZED(401, "C007", "인증이 필요합니다."),
    DUPLICATE_ENTITY(409, "C008", "중복된 엔티티입니다."),

    DO_NOT_LIKE_MY_COMMENT(400, "C009", "자신의 댓글에 좋아요할 수 없습니다."),
    DO_NOT_LIKE_MY_POST(400, "C010", "자신의 게시물에 좋아요할 수 없습니다.");

    private final int statusCode;
    private final String code;
    private final String message;
}