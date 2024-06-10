package com.sparta.springnewsfeed.comment;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
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
    private final UserRepository userRepository;

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

//
//
//    @Transactional(readOnly = true)
//    public List<CommentResponseDto> getComments() {
//        return commentRepository.findAllByOrderByModifiedAt().stream().map(CommentResponseDto::new).toList(); // Query Method 사용 가장 최근 댓글 최근
//    }
//
//    // DB 생성
//    public CommentResponseDto addComment(CommentRequestDto requestDto) {
//
//        // RequestDto -> Entity
//        Comment comment = new Comment(requestDto);
//
//        // DB 저장
//        Comment saveComment = commentRepository.save(comment);
//
//        // Entity -> ResponseDto
//        return new CommentResponseDto(saveComment);
//    }
//
//    // DB 수정
//    @Transactional
//    public Long updateComment(Long id, CommentRequestDto requestDto) {
//        // 해당 메모가 DB에 존재하는지 확인
//        Comment comment = findComment(id);
//        comment.update(requestDto);
//        return id;
//    }
//
//    // 댓글 삭제
//    public Long deleteComment(Long id) {
//        Comment comment = findComment(id);
//        commentRepository.delete(comment);
//        return id;
//    }
//
//    private Comment findComment(Long id) {
//        return commentRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
//        );
//
    }
