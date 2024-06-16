package io.sillysillyman.springnewsfeed.like.commentlike;

import io.sillysillyman.springnewsfeed.auth.UserDetailsImpl;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
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
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/{postId}/comment/{commentId}/like")
    public HttpStatusResponseDto doLike(@PathVariable Long postId, @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.doLike(postId, commentId, userDetails.getUser().getId());
    }

    @DeleteMapping("/{postId}/comment/{commentId}/like")
    public HttpStatusResponseDto undoLike(@PathVariable Long postId, @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.undoLike(postId, commentId, userDetails.getUser().getId());
    }
}
