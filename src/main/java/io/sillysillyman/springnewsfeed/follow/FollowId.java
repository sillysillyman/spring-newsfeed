package io.sillysillyman.springnewsfeed.follow;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FollowId implements Serializable {

    private Long followerId;
    private Long followeeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FollowId followId = (FollowId) o;
        return Objects.equals(followerId, followId.followerId) &&
            Objects.equals(followeeId, followId.followeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followeeId);
    }
}