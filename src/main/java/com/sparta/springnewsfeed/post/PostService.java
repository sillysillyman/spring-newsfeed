package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.follow.Follow;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return new PostResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(PostResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return postRepository.findByUser(user).stream().map(PostResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsOfFollowees(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Follow> followees = user.getFollowing();
        List<PostResponse> postResponses = new ArrayList<>();
        for (var followee : followees) {
            User follwedUser = followee.getFollowed();
            List<Post> posts = postRepository.findByUser(follwedUser);

            for (Post post : posts) {
                postResponses.add(new PostResponse(post));
            }
        }
        return postResponses;
    }

    @Transactional
    public PostResponse updatePost(PostRequest request) {
        Post post = postRepository.findById(request.getPostId())
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

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
