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

    @PostMapping("/{postId}/comment/like")
    public HttpStatusResponseDto doLike(@PathVariable Long postId, @RequestParam Long userId) {
        return commentLikeService.doLike(postId, userId);
    }

    @DeleteMapping("/{postId}/comment/like")
    public HttpStatusResponseDto undoLike(@PathVariable Long postId, @RequestParam Long userId) {
        return commentLikeService.undoLike(postId, userId);
    }
}
