package io.sillysillyman.springnewsfeed.follow;

import io.sillysillyman.springnewsfeed.common.ResponseCode;
import io.sillysillyman.springnewsfeed.user.User;
import io.sillysillyman.springnewsfeed.user.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseCode followUser(String followerUsername, String followeeUsername) {
        Optional<User> optionalFollower = userRepository.findByUsername(followerUsername);
        Optional<User> optionalFollowee = userRepository.findByUsername(followeeUsername);

        if (optionalFollower.isEmpty() || optionalFollowee.isEmpty()) {
            return ResponseCode.ENTITY_NOT_FOUND;
        }

        User follower = optionalFollower.get();
        User followee = optionalFollowee.get();

        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            return ResponseCode.DUPLICATE_ENTITY;
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        follow.setFollowerId(follower.getId());
        follow.setFolloweeId(followee.getId());

        followRepository.save(follow);

        return ResponseCode.SUCCESS;
    }

    // 팔로우 취소
    @Transactional
    public ResponseCode unfollowUser(String followerUsername, String followedUsername) {
        Optional<User> optionalFollower = userRepository.findByUsername(followerUsername);
        Optional<User> optionalFollowee = userRepository.findByUsername(followedUsername);

        if (optionalFollower.isEmpty() || optionalFollowee.isEmpty()) {
            return ResponseCode.ENTITY_NOT_FOUND;
        }

        User follower = optionalFollower.get();
        User followee = optionalFollowee.get();

        Optional<Follow> followOptional = followRepository.findByFollowerAndFollowee(follower,
            followee);

        if (followOptional.isEmpty()) {
            return ResponseCode.INVALID_INPUT_VALUE;
        }

        followRepository.delete(followOptional.get());

        return ResponseCode.SUCCESS;
    }
}
