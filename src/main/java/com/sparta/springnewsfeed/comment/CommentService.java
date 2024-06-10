package com.sparta.springnewsfeed.comment;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment addComment(Long postId, CommentRequestDto requestDto, User user) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(postId).get());
        comment.setUser(user);
        comment.setContent(requestDto.getCommentContents());
        return commentRepository.save(comment);
    }

    // 특정 게시글에 대한 댓글 조회
    @Transactional(readOnly = true)
    public HttpStatusResponseDto getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllById(Collections.singleton(postId));
        if (comments.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.SUCCESS, "작성하신 댓글이 없습니다.");
        }
        return new HttpStatusResponseDto(ResponseCode.SUCCESS, comments);
    }

    // 댓글 수정
    @Transactional
    public HttpStatusResponseDto updateComment(User author, Long commentId, CommentRequestDto requestDto) {
        if (!commentRepository.existsById(commentId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != author.getId()) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE, "본인이 작성한 댓글만 수정이 가능합니다.");
        }
        comment.setContent(requestDto.getCommentContents());
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    // 댓글 삭제
    public HttpStatusResponseDto deleteComment(User author, Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != author.getId()) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE, "본인이 작성한 댓글만 삭제 가능합니다.");
        }
        commentRepository.delete(comment);
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

}
