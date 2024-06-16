package io.sillysillyman.springnewsfeed.post;

import io.sillysillyman.springnewsfeed.auth.JwtUtil;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.common.ResponseCode;
import io.sillysillyman.springnewsfeed.follow.Follow;
import io.sillysillyman.springnewsfeed.post.dto.PostRequestDto;
import io.sillysillyman.springnewsfeed.post.dto.PostResponseDto;
import io.sillysillyman.springnewsfeed.user.User;
import io.sillysillyman.springnewsfeed.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public HttpStatusResponseDto<PostResponseDto> createPost(Post post) {
        postRepository.save(post);
        return new HttpStatusResponseDto<>(ResponseCode.CREATED, new PostResponseDto(post));
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto<?> getAllPosts() {
        List<PostResponseDto> posts = postRepository.findAll().stream().map(PostResponseDto::new)
            .toList();
        if (posts.isEmpty()) {
            return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, "먼저 작성하여 소식을 알려보세요");
        }
        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, posts);
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto<?> getPostById(String username, Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().getUsername()
            .equals(username)) {
            return new HttpStatusResponseDto<>(ResponseCode.ENTITY_NOT_FOUND);
        }
        Post post = optionalPost.get();
        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, new PostResponseDto(post));
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto<?> getPostsByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto<>(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();

        List<PostResponseDto> posts = postRepository.findByUser(user).stream()
            .map(PostResponseDto::new)
            .toList();

        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, posts);
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto<?> getPostsOfFollowees(String token, String username) {
        if (!isValidUser(token, username)) {
            return new HttpStatusResponseDto<>(ResponseCode.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto<>(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();

        List<Follow> followees = user.getFollowees();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (var followee : followees) {
            User follweeUser = followee.getFollowee();
            List<Post> posts = postRepository.findByUser(follweeUser);

            for (Post post : posts) {
                postResponseDtos.add(new PostResponseDto(post));
            }
        }
        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, postResponseDtos);
    }

    @Transactional
    public HttpStatusResponseDto<?> updatePost(User user, Long postId, PostRequestDto request) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().getUsername()
            .equals(user.getUsername())) {
            return new HttpStatusResponseDto<>(ResponseCode.ENTITY_NOT_FOUND);
        }

        Post post = optionalPost.get();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        Post updatedPost = postRepository.save(post);
        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS, new PostResponseDto(updatedPost));
    }

    @Transactional
    public HttpStatusResponseDto<Void> deletePost(User user, Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().getUsername()
            .equals(user.getUsername())) {
            return new HttpStatusResponseDto<>(ResponseCode.ENTITY_NOT_FOUND);
        }

        postRepository.delete(optionalPost.get());
        return new HttpStatusResponseDto<>(ResponseCode.SUCCESS);
    }

    private boolean isValidUser(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return tokenUsername != null && tokenUsername.equals(username);
    }

    private String getUsernameFromToken(String token) {
        try {
            return jwtUtil.getUsernameFromJwt(token);
        } catch (Exception e) {
            return null;
        }
    }
}