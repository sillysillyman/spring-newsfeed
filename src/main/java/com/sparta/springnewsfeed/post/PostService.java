package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.follow.Follow;
import com.sparta.springnewsfeed.security.JwtTokenProvider;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public HttpStatusResponseDto createPost(String token, Long userId, PostRequest request) {
        if (isValidUser(token, userId)) {
            return new HttpStatusResponseDto(ResponseCode.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();

        Post post = new Post(request.getTitle(), request.getContent(), user);
        Post savedPost = postRepository.save(post);

        return new HttpStatusResponseDto(ResponseCode.CREATED, new PostResponse(savedPost));
    }


    @Transactional(readOnly = true)
    public HttpStatusResponseDto getAllPosts() {
        List<PostResponse> posts = postRepository.findAll().stream().map(PostResponse::new)
            .toList();
        if (posts.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.SUCCESS, "먼저 작성하여 소식을 알려보세요");
        }
        return new HttpStatusResponseDto(ResponseCode.SUCCESS, posts);
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto getPostsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();

        List<PostResponse> posts = postRepository.findByUser(user).stream().map(PostResponse::new)
            .toList();

        return new HttpStatusResponseDto(ResponseCode.SUCCESS, posts);
    }

    @Transactional(readOnly = true)
    public HttpStatusResponseDto getPostsOfFollowees(String token, Long userId) {
        if (!isValidUser(token, userId)) {
            return new HttpStatusResponseDto(ResponseCode.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();

        List<Follow> followees = user.getFollowing();
        List<PostResponse> postResponses = new ArrayList<>();
        for (var followee : followees) {
            User follwedUser = followee.getFollowed();
            List<Post> posts = postRepository.findByUser(follwedUser);

            for (Post post : posts) {
                postResponses.add(new PostResponse(post));
            }
        }
        return new HttpStatusResponseDto(ResponseCode.SUCCESS, postResponses);
    }

    @Transactional
    public HttpStatusResponseDto updatePost(String token, Long userId, Long postId,
        PostRequest request) {
        if (!isValidUser(token, userId)) {
            return new HttpStatusResponseDto(ResponseCode.UNAUTHORIZED);
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().getId().equals(userId)) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        Post post = optionalPost.get();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        Post updatedPost = postRepository.save(post);
        return new HttpStatusResponseDto(ResponseCode.SUCCESS, new PostResponse(updatedPost));
    }

    @Transactional
    public HttpStatusResponseDto deletePost(String token, Long userId, Long postId) {
        if (!isValidUser(token, userId)) {
            return new HttpStatusResponseDto(ResponseCode.UNAUTHORIZED);
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty() || !optionalPost.get().getUser().getId().equals(userId)) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        postRepository.delete(optionalPost.get());
        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }

    private boolean isValidUser(String token, Long userId) {
        Long tokenUserId = getUserIdFromToken(token);
        return tokenUserId != null && tokenUserId.equals(userId);
    }

    private Long getUserIdFromToken(String token) {
        try {
            return Long.valueOf(jwtTokenProvider.getUserIdFromJwt(token));
        } catch (Exception e) {
            return null;
        }
    }
}