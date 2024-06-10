package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/{postId}/comment/{commentId}/like")
    public HttpStatusResponseDto doLike(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam Long userId) {
        return commentLikeService.doLike(postId, commentId, userId);
    }

    @DeleteMapping("/{postId}/comment/{commentId}/like")
    public HttpStatusResponseDto undoLike(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam Long userId) {
        return commentLikeService.undoLike(postId, commentId, userId);
    }
}
