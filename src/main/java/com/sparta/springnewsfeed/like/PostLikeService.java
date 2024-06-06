package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "유효하지 않은 입력 값입니다.");
        }

        // 예외 1) 이미 해당 userId-postId 로 좋아요 등록되어있는 경우
        if (postLikeRepository.findByIdUserIdAndIdPostId(userId, postId).orElse(null) != null) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "중복된 Entity입니다.");
        }

        // 예외 2) 자신의 게시글에 좋아요한 경우
        if (userId.equals(post.getUser().getId())) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "자신의 게시글에 좋아요할 수 없습니다.");
        }

        postLikeRepository.save(new PostLike(new PostLikeId(userId, postId) ,user, post));
        return new HttpStatusResponseDto(HttpStatus.OK.toString(), "요청이 성공했습니다.");
    }

    // 게시글 좋아요 취소 (삭제)
    public HttpStatusResponseDto undoLike(Long postId, Long userId) {
        // 사용자 요청 userId-postId가 유효한지 검색
        PostLike postLike = postLikeRepository.findByIdUserIdAndIdPostId(userId, postId).orElse(null);
        if (postLike == null) {
            return new HttpStatusResponseDto(HttpStatus.BAD_REQUEST.toString(), "유효하지 않은 입력 값입니다.");
        }

        postLikeRepository.delete(postLike);
        return new HttpStatusResponseDto(HttpStatus.OK.toString(), "요청이 성공했습니다.");
    }

}
