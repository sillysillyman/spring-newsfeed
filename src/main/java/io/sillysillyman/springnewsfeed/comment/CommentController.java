package io.sillysillyman.springnewsfeed.comment;

import io.sillysillyman.springnewsfeed.auth.UserDetailsImpl;
import io.sillysillyman.springnewsfeed.comment.dto.CommentRequestDto;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class CommentController {

    private final CommentService commentService;


    // 댓글 생성, 수정, 조회, 삭제 기능
    @PostMapping("/{postId}/comments")
    public HttpStatusResponseDto addComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @RequestBody CommentRequestDto requestDto) {
        // 현재 인증된 사용자를 가져와서 댓글의 작성자로 설정
        User author = userDetails.getUser();

        return commentService.createComment(postId, author, requestDto);
    }

    @GetMapping("/{postId}/comments")
    public HttpStatusResponseDto getCommentsByCommentId(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public HttpStatusResponseDto updateComment(@PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User author = userDetails.getUser();
        return commentService.updateComment(author, commentId, requestDto);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public HttpStatusResponseDto deleteComment(@PathVariable Long commentId,
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User author = userDetails.getUser();
        return commentService.deleteComment(author, commentId);
    }
}