package com.sparta.springnewsfeed.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "S001", "Success"),
    CREATED(201, "S002", "Created"),
    INVALID_INPUT_VALUE(400, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method not allowed"),
    ENTITY_NOT_FOUND(404, "C003", "Entity not found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Internal server error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid type value"),
    ACCESS_DENIED(403, "C006", "Access denied"),
    UNAUTHORIZED(401, "C007", "Unauthorized"),
    DUPLICATE_ENTITY(409, "C008", "Duplicate entity");

    private final int statusCode;
    private final String code;
    private final String message;
}