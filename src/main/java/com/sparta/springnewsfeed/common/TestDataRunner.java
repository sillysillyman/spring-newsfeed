package com.sparta.springnewsfeed.common;

import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.like.PostLikeRepository;
import com.sparta.springnewsfeed.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    PostLikeRepository postLikeRepository;
    @Autowired
    PostRepository postRepository;

    @Override
    public void run(ApplicationArguments args) {
        // 테스트 Post 생성
        // post 1
        Post testPost1 = new Post();
        testPost1.setUserId(1L);
        testPost1.setTitle("Test Post111");
        testPost1.setContent("This is a test post111");
        testPost1 = postRepository.save(testPost1);

        // post 2
        Post testPost2 = new Post();
        testPost2.setUserId(1L);
        testPost2.setTitle("Test Post222");
        testPost2.setContent("This is a test post222");
        testPost2 = postRepository.save(testPost2);

    }

}