package com.sparta.springnewsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank
    private String title;

    @NotNull
    private String content;
}
