package io.sillysillyman.springnewsfeed.post.dto;

import io.sillysillyman.springnewsfeed.post.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long postId;

    private final String username;

    private final String title;

    private final String content;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}