package com.sparta.springnewsfeed;

import com.sparta.springnewsfeed.comment.Comment;
import com.sparta.springnewsfeed.comment.CommentRepository;
import com.sparta.springnewsfeed.like.CommentLike;
import com.sparta.springnewsfeed.like.CommentLikeId;
import com.sparta.springnewsfeed.like.CommentLikeRepository;
import com.sparta.springnewsfeed.like.PostLike;
import com.sparta.springnewsfeed.like.PostLikeId;
import com.sparta.springnewsfeed.like.PostLikeRepository;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import com.sparta.springnewsfeed.user.UserStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class SpringNewsfeedApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Autowired
    PostLikeRepository postLikeRepository;

    @Test
    @DisplayName("user 테스트 데이터 추가")
    @Transactional
    @Rollback(value = false)
    void test() {

        // 사용자 db 등록
        User user = new User();
        user.setName("이름");
        user.setUserid("aaa");
        user.setPassword("1234");
        user.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user);

        // 게시글 db 등록
        Post post = new Post();
        post.setTitle("게시글제목");
        post.setContent("게시글내용");
        post.setUser(user);
        postRepository.save(post);

        // 댓글 db 등록
        Comment comment = new Comment();
        comment.setContent("댓글내용");
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);

        // 게시글 좋아요 db 등록
        PostLike postLike = new PostLike(
                new PostLikeId(user.getId(), post.getId()),
                user, post);
        postLikeRepository.save(postLike);

        // 댓글 좋아요 db 등록
        CommentLike commentLike = new CommentLike(
                new CommentLikeId(user.getId(), comment.getId()),
                user, comment);
        commentLikeRepository.save(commentLike);

    }

}
