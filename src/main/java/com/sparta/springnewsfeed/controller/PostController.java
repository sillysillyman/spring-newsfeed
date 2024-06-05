package com.sparta.springnewsfeed.controller;

import com.sparta.springnewsfeed.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PostController {

    public final PostService postService;
}
