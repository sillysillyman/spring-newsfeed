package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("사용자를 찾지 못 했습니다."));

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return new PostResponse(savedPost);
    }

    @Transactional
    public PostResponse updatePost(PostRequest request) {
        Post post = postRepository.findById(request.getPostId())
            .orElseThrow(() -> new RuntimeException("게시물을 찾지 못 했습니다."));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);

        return new PostResponse(updatedPost);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾지 못 했습니다."));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시물을 찾지 못 했습니다."));

        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new RuntimeException("게시물과 사용자가 일치하지 않습니다.");
        }

        postRepository.delete(post);
    }
}
