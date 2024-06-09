package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
}
