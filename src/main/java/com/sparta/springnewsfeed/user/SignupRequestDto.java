package com.sparta.springnewsfeed.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String userid;

    private String password;

    private String email;
}
