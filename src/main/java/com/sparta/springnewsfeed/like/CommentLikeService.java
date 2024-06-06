package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.comment.Comment;
import com.sparta.springnewsfeed.comment.CommentRepository;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 좋아요 등록
    public HttpStatusResponseDto doLike(Long commentId, Long userId) {

        // 예외 0) commentId, userId가 유효한지 확인 (등록되어있는 commentId, userId인지 확인)
        Comment comment = commentRepository.findById(commentId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (comment == null || user == null) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "유효하지 않은 입력 값입니다.");
        }

        // 예외 1) 이미 해당 userId-commentId 로 좋아요 등록되어있는 경우
        if (commentLikeRepository.findByIdUserIdAndIdCommentId(userId, commentId).orElse(null) != null) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "중복된 Entity입니다.");
        }

        // 예외 2) 자신의 댓글에 좋아요한 경우
        if (userId.equals(comment.getUser().getId())) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "자신의 댓글에 좋아요할 수 없습니다.");
        }

        commentLikeRepository.save(new CommentLike(new CommentLikeId(userId, commentId), user, comment));
        return new HttpStatusResponseDto(HttpStatus.OK.toString(), "요청이 성공했습니다.");
    }

    // 댓글 좋아요 취소 (삭제)
    public HttpStatusResponseDto undoLike(Long commentId, Long userId) {
        // 사용자 요청 userId-commentId가 유효한지 검색
        CommentLike commentLike = commentLikeRepository.findByIdUserIdAndIdCommentId(userId, commentId).orElse(null);
        if (commentLike == null) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "유효하지 않은 입력 값입니다.");
        }

        commentLikeRepository.delete(commentLike);
        return new HttpStatusResponseDto(HttpStatus.OK.toString(), "요청이 성공했습니다.");
    }
}
