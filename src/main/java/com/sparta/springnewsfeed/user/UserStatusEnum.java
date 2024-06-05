package com.sparta.springnewsfeed.user;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    VERIFIED("Verified"), // 인증된 상태
    UNVERIFIED("Unverified"), // 인증전 상태
    DELETED("Deleted"); // 탈퇴한 상태

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

}