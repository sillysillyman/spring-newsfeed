package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/{userId}/posts")
    public HttpStatusResponseDto createPost(@PathVariable Long userId,
        @RequestBody PostRequest postRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.extractToken(request);
        return postService.createPost(token, userId, postRequest);
    }

    @GetMapping("/posts")
    public HttpStatusResponseDto getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{userId}/following/posts")
    public HttpStatusResponseDto getPostsOfFollowees(@PathVariable Long userId,
        HttpServletRequest request) {
        String token = jwtTokenProvider.extractToken(request);
        return postService.getPostsOfFollowees(token, userId);
    }

    @GetMapping("/{userId}/posts")
    public HttpStatusResponseDto getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto getPostById(@PathVariable Long userId, @PathVariable Long postId) {
        return postService.getPostById(userId, postId);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto updatePost(@PathVariable Long userId, @PathVariable Long postId,
        @RequestBody PostRequest postRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.extractToken(request);
        return postService.updatePost(token, userId, postId, postRequest);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto deletePost(@PathVariable Long userId, @PathVariable Long postId,
        HttpServletRequest request) {
        String token = jwtTokenProvider.extractToken(request);
        return postService.deletePost(token, userId, postId);
    }
}