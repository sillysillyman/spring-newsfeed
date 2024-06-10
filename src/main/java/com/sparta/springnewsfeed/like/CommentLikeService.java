package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.comment.Comment;
import com.sparta.springnewsfeed.comment.CommentRepository;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 좋아요 등록
    public HttpStatusResponseDto doLike(Long postId, Long commentId, Long userId) {
        if (!checkInputs(postId, commentId, userId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        // 예외 1) 이미 해당 userId-commentId 로 좋아요 등록되어있는 경우
        if (commentLikeRepository.existsByIdUserIdAndIdCommentId(userId, commentId)) {
            return new HttpStatusResponseDto(ResponseCode.DUPLICATE_ENTITY);
        }

        // 예외 2) 자신의 댓글에 좋아요한 경우
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (userId.equals(comment.getUser().getId())) {
            return new HttpStatusResponseDto(ResponseCode.DO_NOT_LIKE_MY_COMMENT);
        }

        User user = userRepository.findById(userId).orElse(null);
        commentLikeRepository.save(new CommentLike(new CommentLikeId(userId, commentId), user, comment));
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    // 댓글 좋아요 취소 (삭제)
    public HttpStatusResponseDto undoLike(Long postId, Long commentId, Long userId) {
        if (!checkInputs(postId, commentId, userId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        // 사용자 요청 userId-commentId가 유효한지 검색
        CommentLike commentLike = commentLikeRepository.findByIdUserIdAndIdCommentId(userId, commentId).orElse(null);
        if (commentLike == null) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        commentLikeRepository.delete(commentLike);
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    private boolean checkInputs(Long postId, Long commentId, Long userId) {
        // postId, commentId, userId가 유효한지 확인 (등록되어있는 postId, commentId, userId인지 확인)
        if (postRepository.existsById(postId) && commentRepository.existsById(commentId) && userRepository.existsById(userId)) {
            return true;
        }
        return false;
    }
}