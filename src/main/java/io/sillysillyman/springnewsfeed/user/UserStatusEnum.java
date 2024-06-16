package io.sillysillyman.springnewsfeed.user;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    VERIFIED("Verified"), // 인증
    UNVERIFIED("Unverified"), // 미인증
    DELETED("Deleted"); // 탈퇴

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

}