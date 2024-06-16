package io.sillysillyman.springnewsfeed.post;

import io.sillysillyman.springnewsfeed.auth.JwtUtil;
import io.sillysillyman.springnewsfeed.auth.UserDetailsImpl;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.post.dto.PostRequestDto;
import io.sillysillyman.springnewsfeed.post.dto.PostResponseDto;
import io.sillysillyman.springnewsfeed.user.User;
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
    public HttpStatusResponseDto<PostResponseDto> createPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody Post post) {
        // 현재 인증된 사용자를 가져와 게시글의 작성자로 설정
        User author = userDetails.getUser();
        post.setUser(author);
        // 게시글 저장
        return postService.createPost(post);
    }

    @GetMapping("/posts")
    public HttpStatusResponseDto<?> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{username}/following/posts")
    public HttpStatusResponseDto<?> getPostsOfFollowees(@PathVariable String username,
        HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromHeader(request);
        return postService.getPostsOfFollowees(token, username);
    }

    @GetMapping("/{username}/posts")
    public HttpStatusResponseDto<?> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    @GetMapping("/{username}/posts/{postId}")
    public HttpStatusResponseDto<?> getPostById(@PathVariable String username,
        @PathVariable Long postId) {
        return postService.getPostById(username, postId);
    }

    @PutMapping("/posts/{postId}")
    public HttpStatusResponseDto<?> updatePost(@PathVariable Long postId,
        @RequestBody PostRequestDto postRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        return postService.updatePost(user, postId, postRequestDto);
    }

    @DeleteMapping("/posts/{postId}")
    public HttpStatusResponseDto<Void> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.deletePost(user, postId);
    }
}