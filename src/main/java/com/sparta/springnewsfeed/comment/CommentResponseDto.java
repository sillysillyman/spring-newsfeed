package com.sparta.springnewsfeed.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long commentId; // 댓글 ID
    private String username;
    private String commentContents;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        // 게시물 ID - 댓글 ID
        this.postId = comment.getPost().getId();
        this.commentId = comment.getCommentId();

        // 실제 보여지는 것
        this.username = comment.getUser().getName();
        this.commentContents = comment.getContent();

        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getModifiedAt();
    }
}