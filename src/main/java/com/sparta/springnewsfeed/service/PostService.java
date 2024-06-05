package com.sparta.springnewsfeed.service;

import com.sparta.springnewsfeed.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
}
