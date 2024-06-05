package com.sparta.springnewsfeed.dto;

import com.sparta.springnewsfeed.entity.Post;
import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
