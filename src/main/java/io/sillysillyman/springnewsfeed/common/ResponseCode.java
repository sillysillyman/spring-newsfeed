package io.sillysillyman.springnewsfeed.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "요청이 성공했습니다."),
    CREATED(201, "리소스가 성공적으로 생성되었습니다."),
    INVALID_INPUT_VALUE(400, "유효하지 않은 입력 값입니다."),
    CANNOT_LIKE_MY_COMMENT(400, "자신의 댓글에 좋아요를 누를 수 없습니다."),
    CANNOT_LIKE_MY_POST(400, "자신의 게시물에 좋아요를 누를 수 없습니다."),
    INVALID_TYPE_VALUE(400, "유효하지 않은 타입 값입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    ACCESS_DENIED(403, "접근이 거부되었습니다."),
    ENTITY_NOT_FOUND(404, "엔티티를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않는 메서드입니다."),
    DUPLICATE_ENTITY(409, "중복된 엔티티입니다."),
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류입니다.");

    private final int statusCode;
    private final String message;
}