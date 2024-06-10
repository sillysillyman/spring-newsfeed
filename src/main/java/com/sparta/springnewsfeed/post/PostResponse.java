package com.sparta.springnewsfeed.post;

import lombok.Getter;

@Getter
public class PostResponse {

    private Long postId;

    private String title;

    private String content;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
