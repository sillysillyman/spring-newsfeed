package io.sillysillyman.springnewsfeed.follow;

import io.sillysillyman.springnewsfeed.auth.UserDetailsImpl;
import io.sillysillyman.springnewsfeed.common.HttpStatusResponseDto;
import io.sillysillyman.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 팔로우 하기
    @PostMapping("/follow/{followeeUsername}")
    public ResponseEntity<HttpStatusResponseDto> followUser(
        @PathVariable String followeeUsername,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String followerUsername = userDetails.getUser().getUsername();
        ResponseCode responseCode = followService.followUser(followerUsername, followeeUsername);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 팔로우 취소
    @DeleteMapping("/unfollow/{followeeUsername}")
    public ResponseEntity<HttpStatusResponseDto> unfollowUser(
        @PathVariable String followeeUsername,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String followerUsername = userDetails.getUser().getUsername();
        ResponseCode responseCode = followService.unfollowUser(followerUsername, followeeUsername);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }
}
