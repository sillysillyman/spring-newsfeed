package io.sillysillyman.springnewsfeed.post;

import io.sillysillyman.springnewsfeed.auth.UserDetailsImpl;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.post.dto.PostRequestDto;
import io.sillysillyman.springnewsfeed.post.dto.PostResponseDto;
import io.sillysillyman.springnewsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public HttpStatusResponseDto<PostResponseDto> createPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PostRequestDto postRequestDto) {
        User user = userDetails.getUser();
        return postService.createPost(user, postRequestDto);
    }

    @GetMapping
    public HttpStatusResponseDto<?> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{username}/following")
    public HttpStatusResponseDto<?> getPostsOfFollowees(@PathVariable String username) {
        return postService.getPostsOfFollowees(username);
    }

    @GetMapping("/{username}")
    public HttpStatusResponseDto<?> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    @GetMapping("/{username}/{postId}")
    public HttpStatusResponseDto<?> getPostById(@PathVariable String username,
        @PathVariable Long postId) {
        return postService.getPostById(username, postId);
    }

    @PutMapping("/{username}/{postId}")
    public HttpStatusResponseDto<?> updatePost(@PathVariable String username,
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PostRequestDto postRequestDto) {
        User user = userDetails.getUser();
        return postService.updatePost(username, postId, user, postRequestDto);
    }

    @DeleteMapping("/{username}/{postId}")
    public HttpStatusResponseDto<Void> deletePost(@PathVariable String username,
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.deletePost(username, postId, user);
    }
}