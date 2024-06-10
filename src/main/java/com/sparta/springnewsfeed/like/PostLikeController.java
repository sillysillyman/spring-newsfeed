package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public HttpStatusResponseDto doLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postLikeService.doLike(postId, userDetails.getUser().getId());
    }

    @DeleteMapping("/{postId}/like")
    public HttpStatusResponseDto undoLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postLikeService.undoLike(postId, userDetails.getUser().getId());
    }

}