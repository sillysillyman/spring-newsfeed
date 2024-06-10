package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 좋아요 등록
    public HttpStatusResponseDto doLike(Long postId, Long userId) {

        // 예외 0) postId, userId가 유효한지 확인 (등록되어있는 postId, userId인지 확인)
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (post == null || user == null) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        // 예외 1) 이미 해당 userId-postId 로 좋아요 등록되어있는 경우
        if (postLikeRepository.existsByIdUserIdAndIdPostId(userId, postId)) {
            return new HttpStatusResponseDto(ResponseCode.DUPLICATE_ENTITY);
        }

        // 예외 2) 자신의 게시글에 좋아요한 경우
        if (userId.equals(post.getUser().getId())) {
            return new HttpStatusResponseDto(ResponseCode.DO_NOT_LIKE_MY_POST);
        }

        postLikeRepository.save(new PostLike(new PostLikeId(userId, postId) ,user, post));
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    // 게시글 좋아요 취소 (삭제)
    public HttpStatusResponseDto undoLike(Long postId, Long userId) {
        // postId, userId가 유효한지 확인 (등록되어있는 postId, userId인지 확인)
        if (postRepository.existsById(postId) && userRepository.existsById(userId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        // 사용자 요청 userId-postId가 유효한지 검색
        PostLike postLike = postLikeRepository.findByIdUserIdAndIdPostId(userId, postId).orElse(null);
        if (postLike == null) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }

        postLikeRepository.delete(postLike);
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }
}
