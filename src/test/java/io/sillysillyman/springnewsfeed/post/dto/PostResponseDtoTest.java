package io.sillysillyman.springnewsfeed.post.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sillysillyman.springnewsfeed.post.Post;
import io.sillysillyman.springnewsfeed.user.User;
import org.junit.jupiter.api.Test;

public class PostResponseDtoTest {

    @Test
    public void should_CreatePostResponseDto_when_ValidPostProvided() {
        //given
        User user = new User();
        user.setUsername("testUser");
        String title = "Test Title";
        String content = "Test Content";
        Post post = new Post(title, content, user);

        // when
        PostResponseDto postResponseDto = new PostResponseDto(post);

        // then
        assertNotNull(postResponseDto);
        assertEquals(title, postResponseDto.getTitle());
        assertEquals(content, postResponseDto.getContent());
        assertEquals(user.getUsername(), postResponseDto.getUsername());
    }
}
