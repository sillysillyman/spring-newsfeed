package com.sparta.springnewsfeed.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequestDto {
    private String userId;
    private String password;
}
