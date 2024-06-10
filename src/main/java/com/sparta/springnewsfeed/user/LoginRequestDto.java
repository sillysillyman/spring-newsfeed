package com.sparta.springnewsfeed.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String userid;
    private String password;
}