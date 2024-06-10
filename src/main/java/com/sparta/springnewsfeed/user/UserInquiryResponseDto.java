package com.sparta.springnewsfeed.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInquiryResponseDto {

    private String userId;
    private String name;
    private String introduction;
    private String email;
    private String pictureURL;
}
