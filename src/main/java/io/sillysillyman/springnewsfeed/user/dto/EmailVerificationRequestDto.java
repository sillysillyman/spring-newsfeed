package io.sillysillyman.springnewsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationRequestDto {

    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String code;
}