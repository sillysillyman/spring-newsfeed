package io.sillysillyman.springnewsfeed.follow;

import io.sillysillyman.springnewsfeed.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
}
