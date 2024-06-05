package com.sparta.springnewsfeed.controller;

import com.sparta.springnewsfeed.dto.HttpStatusResponseDto;
import com.sparta.springnewsfeed.service.PostLikeService;
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
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public HttpStatusResponseDto doLike(@PathVariable Long postId, @RequestParam Long userId) {
        return postLikeService.doLike(postId, userId);
    }

    @DeleteMapping("/{postId}/like")
    public HttpStatusResponseDto undoLike(@PathVariable Long postId, @RequestParam Long userId) {
        return postLikeService.undoLike(postId, userId);
    }

}
