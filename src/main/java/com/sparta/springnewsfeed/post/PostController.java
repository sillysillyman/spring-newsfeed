package com.sparta.springnewsfeed.post;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PostController {

    public final PostService postService;
}
