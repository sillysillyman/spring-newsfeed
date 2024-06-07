package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("사용자를 찾지 못 했습니다."));
        Post post = new Post(request.getTitle(), request.getContent(), user);
        
        return new PostResponse(postRepository.save(post));
    }
}
