package io.sillysillyman.springnewsfeed.post.dto;

import io.sillysillyman.springnewsfeed.post.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long postId;

    private String title;

    private String content;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}