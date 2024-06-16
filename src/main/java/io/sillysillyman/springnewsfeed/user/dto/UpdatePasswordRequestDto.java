package io.sillysillyman.springnewsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequestDto {

    @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
    private String currentPassword;

    @Size(min = 10, message = "새 비밀번호는 최소 10글자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!*()@#$%^&+=]).+$", message = "새 비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String newPassword;
}
