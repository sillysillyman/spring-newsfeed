package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /*@PostMapping("/{userId}/posts")
    public HttpStatusResponseDto createPost(@PathVariable String userId,
        @RequestBody PostRequest postRequest, HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.createPost(token, userId, postRequest);
    }


*/
    /*@PostMapping("/{userId}/posts")
    public ResponseEntity<String> createPost(@PathVariable String userId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody Post post) {
        // 현재 인증된 사용자를 가져와 사용자 ID가 일치하는지 확인
        User currentUser = userDetails.getUser();

        if (!currentUser.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인된 사용자와 다른 사용자입니다");
        }

        // 작성자 설정 및 게시글 저장
        post.setUser(currentUser);
        postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body("포스트 생성 성공");
    }*/

    @PostMapping("/posts")
    public Post createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody Post post) {
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