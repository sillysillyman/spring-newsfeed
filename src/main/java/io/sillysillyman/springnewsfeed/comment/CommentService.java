package io.sillysillyman.springnewsfeed.comment;

import io.sillysillyman.springnewsfeed.comment.dto.CommentRequestDto;
import io.sillysillyman.springnewsfeed.comment.dto.CommentResponseDto;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.common.ResponseCode;
import io.sillysillyman.springnewsfeed.post.Post;
import io.sillysillyman.springnewsfeed.post.PostRepository;
import io.sillysillyman.springnewsfeed.user.User;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public HttpStatusResponseDto<?> createComment(Long postId, User user,
        CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return new HttpStatusResponseDto<>(ResponseCode.INVALID_INPUT_VALUE, "잘못된 postId입니다.");
        }
        Comment comment = new Comment(commentRequestDto.getContent(), user, post);
        commentRepository.save(comment);
        return new HttpStatusResponseDto<>(ResponseCode.CREATED, new CommentResponseDto(comment));
    }

    // 특정 게시글에 대한 댓글 조회
    @Transactional(readOnly = true)
    public HttpStatusResponseDto getComments(Long postId) {
        List<CommentResponseDto> comments = commentRepository.findAllById(
                Collections.singleton(postId))
            .stream().map(CommentResponseDto::new).toList();
        if (comments.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.SUCCESS, "작성하신 댓글이 없습니다.");
        }
        return new HttpStatusResponseDto(ResponseCode.SUCCESS, comments);
    }

    // 댓글 수정
    @Transactional
    public HttpStatusResponseDto updateComment(User author, Long commentId,
        CommentRequestDto requestDto) {
        if (!commentRepository.existsById(commentId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != author.getId()) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE,
                "본인이 작성한 댓글만 수정이 가능합니다.");
        }
        comment.setContent(requestDto.getContent());
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    // 댓글 삭제
    public HttpStatusResponseDto deleteComment(User author, Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        }
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId() != author.getId()) {
            return new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE,
                "본인이 작성한 댓글만 삭제 가능합니다.");
        }
        commentRepository.delete(comment);
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

}
