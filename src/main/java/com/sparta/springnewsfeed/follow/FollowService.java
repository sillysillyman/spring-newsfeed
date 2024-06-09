package com.sparta.springnewsfeed.follow;

import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.User;
import com.sparta.springnewsfeed.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // 팔로우 하기
    @Transactional
    public ResponseCode followUser(String followerUserid, String followedUserid) {
        Optional<User> followerOptional = userRepository.findByUserid(followerUserid);
        Optional<User> followedOptional = userRepository.findByUserid(followedUserid);

        if (followerOptional.isEmpty() || followedOptional.isEmpty()) {
            return ResponseCode.ENTITY_NOT_FOUND;
        }

        User follower = followerOptional.get();
        User followed = followedOptional.get();

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            return ResponseCode.DUPLICATE_ENTITY;
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        follow.setFollowerId(follower.getId());
        follow.setFollowedId(followed.getId());

        followRepository.save(follow);

        return ResponseCode.SUCCESS;
    }


}
