package io.sillysillyman.springnewsfeed.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {

    @Size(max = 100, message = "소개글은 최대 100글자까지 입력 가능합니다.")
    @NotNull
    private String introduction;

    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "유효한 URL 형식이어야 합니다.")
    private String pictureUrl;
}
