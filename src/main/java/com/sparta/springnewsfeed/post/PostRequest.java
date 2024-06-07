package com.sparta.springnewsfeed.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotNull
    private String content;
}
