package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    private final PostService postService;
    private final JwtUtil jwtUtil;


    @PostMapping("/posts")
    public Post createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody Post post) {
        // 현재 인증된 사용자를 가져와 게시글의 작성자로 설정
        User author = userDetails.getUser();
        post.setUser(author);
        // 게시글 저장
        return postService.createPost(post);
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
    public HttpStatusResponseDto getPostById(@PathVariable String userId,
        @PathVariable Long postId) {
        return postService.getPostById(userId, postId);
    }

    @PutMapping("/posts/{postId}")
    public HttpStatusResponseDto updatePost(@PathVariable Long postId,
        @RequestBody PostRequest postRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        return postService.updatePost(user, postId, postRequest);
    }

    @DeleteMapping("/posts/{postId}")
    public HttpStatusResponseDto deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.deletePost(user, postId);
    }
}