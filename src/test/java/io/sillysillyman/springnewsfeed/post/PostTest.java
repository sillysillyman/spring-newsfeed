package io.sillysillyman.springnewsfeed.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sillysillyman.springnewsfeed.user.User;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    public void given_UserExists_when_PostCreated_then_PostShouldHaveCorrectUser() {
        // given
        User user = new User();
        user.setUsername("testuser");

        // when
        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("Test Content");
        post.setUser(user);

        // then
        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Content", post.getContent());
        assertEquals("testuser", post.getUser().getUsername());
    }
}