package com.sparta.springnewsfeed.user;

import lombok.Getter;

@Getter
public enum SocialProviderEnum {
    KAKAO("Kakao"),
    NAVER("Naver"),
    GOOGLE("Google");

    private final String provider;

    SocialProviderEnum(String provider) {
        this.provider = provider;
    }
}
