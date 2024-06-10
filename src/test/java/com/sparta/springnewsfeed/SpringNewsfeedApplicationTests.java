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
        User user1 = new User();
        user1.setName("글쓴이");
        user1.setUserId("aaa");
        user1.setPassword("1234");
        user1.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("댓글러");
        user2.setUserId("bbb");
        user2.setPassword("1234");
        user2.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("좋아요사냥꾼");
        user3.setUserId("ccc");
        user3.setPassword("1234");
        user3.setStatus(UserStatusEnum.VERIFIED);
        userRepository.save(user3);

        // 게시글 db 등록
        Post post = new Post();
        post.setTitle("게시글제목");
        post.setContent("게시글내용");
        post.setUser(user1);
        postRepository.save(post);

        // 댓글 db 등록
        Comment comment = new Comment();
        comment.setContent("댓글내용");
        comment.setUser(user2);
        comment.setPost(post);
        commentRepository.save(comment);

        // 게시글 좋아요 db 등록
        PostLike postLike = new PostLike(
                new PostLikeId(user3.getId(), post.getId()),
                user3, post);
        postLikeRepository.save(postLike);

        // 댓글 좋아요 db 등록
        CommentLike commentLike = new CommentLike(
                new CommentLikeId(user3.getId(), comment.getId()),
                user3, comment);
        commentLikeRepository.save(commentLike);

    }

}