package io.sillysillyman.springnewsfeed.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void should_HaveCorrectFields_when_UserIsCreated() {
        // given
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";
        String introduction = "This is a test user.";
        String pictureUrl = "http://example.com/picture.jpg";
        UserStatusEnum status = UserStatusEnum.VERIFIED;
        String refreshToken = "refreshToken";

        // when
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setIntroduction(introduction);
        user.setPictureUrl(pictureUrl);
        user.setStatus(status);
        user.setRefreshToken(refreshToken);

        // then
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(introduction, user.getIntroduction());
        assertEquals(pictureUrl, user.getPictureUrl());
        assertEquals(status, user.getStatus());
        assertEquals(refreshToken, user.getRefreshToken());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    public void should_InitializeEmptyLists_when_UserIsCreated() {
        // given: No specific given clause needed

        // when
        User user = new User();

        // then
        assertNotNull(user.getComments());
        assertEquals(new ArrayList<>(), user.getComments());
        assertNotNull(user.getPosts());
        assertEquals(new ArrayList<>(), user.getPosts());
        assertNotNull(user.getPostLikes());
        assertEquals(new ArrayList<>(), user.getPostLikes());
        assertNotNull(user.getCommentLikes());
        assertEquals(new ArrayList<>(), user.getCommentLikes());
        assertNotNull(user.getFollowees());
        assertEquals(new ArrayList<>(), user.getFollowees());
        assertNotNull(user.getFollowers());
        assertEquals(new ArrayList<>(), user.getFollowers());
    }
}