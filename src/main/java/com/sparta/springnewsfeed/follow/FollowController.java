package com.sparta.springnewsfeed.follow;

import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 팔로우 하기
    @PostMapping("/{followerUserid}/follow/{followedUserid}")
    public ResponseEntity<HttpStatusResponseDto> followUser(
            @PathVariable String followerUserid, @PathVariable String followedUserid) {
        ResponseCode responseCode = followService.followUser(followerUserid, followedUserid);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    // 팔로우 취소
    @DeleteMapping("/{followerUserid}/unfollow/{followedUserid}")
    public ResponseEntity<HttpStatusResponseDto> unfollowUser(
            @PathVariable String followerUserid, @PathVariable String followedUserid) {
        ResponseCode responseCode = followService.unfollowUser(followerUserid, followedUserid);
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }
}
