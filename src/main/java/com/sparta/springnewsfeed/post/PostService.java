package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.follow.Follow;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public HttpStatusResponseDto createPost(PostRequest request) {
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
    public HttpStatusResponseDto getPostsOfFollowees(Long userId) {
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
    public HttpStatusResponseDto updatePost(PostRequest request) {
        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        Post post = optionalPost.get();

        if (!post.getUser().getId().equals(request.getUserId())) {
            return new HttpStatusResponseDto(ResponseCode.ACCESS_DENIED);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);

        return new HttpStatusResponseDto(ResponseCode.SUCCESS, new PostResponse(updatedPost));
    }

    @Transactional
    public HttpStatusResponseDto deletePost(Long userId, Long postId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return new HttpStatusResponseDto(ResponseCode.ENTITY_NOT_FOUND);
        }

        User user = optionalUser.get();
        Post post = optionalPost.get();
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            return new HttpStatusResponseDto(ResponseCode.ACCESS_DENIED);
        }

        postRepository.delete(post);

        return new HttpStatusResponseDto(ResponseCode.SUCCESS);
    }
}