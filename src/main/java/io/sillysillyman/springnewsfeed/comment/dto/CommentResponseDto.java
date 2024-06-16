package io.sillysillyman.springnewsfeed.comment.dto;

import io.sillysillyman.springnewsfeed.comment.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private String username;
    private String content;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        // 게시물 ID - 댓글 ID
        this.postId = comment.getPost().getId();
        this.commentId = comment.getId();

        // 실제 보여지는 것
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();

        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}