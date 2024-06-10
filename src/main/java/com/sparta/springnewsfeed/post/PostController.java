package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/{userId}/posts")
    public HttpStatusResponseDto createPost(@PathVariable String userId,
        @RequestBody PostRequest postRequest, HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.createPost(token, userId, postRequest);
    }

    @GetMapping("/posts")
    public HttpStatusResponseDto getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{userId}/following/posts")
    public HttpStatusResponseDto getPostsOfFollowees(@PathVariable String userId,
        HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.getPostsOfFollowees(token, userId);
    }

    @GetMapping("/{userId}/posts")
    public HttpStatusResponseDto getPostsByUserId(@PathVariable String userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto getPostById(@PathVariable String userId, @PathVariable Long postId) {
        return postService.getPostById(userId, postId);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto updatePost(@PathVariable String userId, @PathVariable Long postId,
        @RequestBody PostRequest postRequest, HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.updatePost(token, userId, postId, postRequest);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public HttpStatusResponseDto deletePost(@PathVariable String userId, @PathVariable Long postId,
        HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.deletePost(token, userId, postId);
    }
}