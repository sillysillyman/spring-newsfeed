package com.sparta.springnewsfeed.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotNull
    private String content;
}
