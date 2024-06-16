package io.sillysillyman.springnewsfeed.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInquiryResponseDto {

    private String username;
    private String introduction;
    private String email;
    private String pictureURL;
}
