package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    public final PostService postService;

    @PostMapping
    public HttpStatusResponseDto createPost(@RequestParam Long userId,
        @RequestBody @Valid PostRequest request) {
        request.setUserId(userId);
        return postService.createPost(request);
    }

    @GetMapping
    public HttpStatusResponseDto getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/users/{userId}")
    public HttpStatusResponseDto getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/users/{userId}/following")
    public HttpStatusResponseDto getPostsOfFollowees(@PathVariable Long userId) {
        return postService.getPostsOfFollowees(userId);
    }

    @PutMapping("/{postId}")
    public HttpStatusResponseDto updatePost(@RequestParam Long userId, @PathVariable Long postId,
        @RequestBody @Valid PostRequest postRequest) {
        postRequest.setUserId(userId);
        postRequest.setPostId(postId);
        return postService.updatePost(postRequest);
    }

    @DeleteMapping("/{postId}")
    public HttpStatusResponseDto deletePost(@RequestParam Long userId, @PathVariable Long postId) {
        return postService.deletePost(userId, postId);
    }
}
