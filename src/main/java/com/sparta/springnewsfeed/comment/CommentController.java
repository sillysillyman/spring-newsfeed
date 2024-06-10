package com.sparta.springnewsfeed.comment;

import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class CommentController {

    // Service 주입
    private final CommentService commentService; // CommentController > CommentService > CommentRepository

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성, 수정, 조회, 삭제 기능
    @PostMapping("/{postId}/comments")
    public Comment addComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @PathVariable Long postId,
                              @RequestBody CommentRequestDto requestDto) {
        // 현재 인증된 사용자를 가져와서 댓글의 작성자로 설정
        User author = userDetails.getUser();

        return commentService.addComment(postId, requestDto, author);
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
    public HttpStatusResponseDto deleteComment(@PathVariable Long commentId, @PathVariable Long postId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User author = userDetails.getUser();
        return commentService.deleteComment(author, commentId);
    }
}