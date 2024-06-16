package io.sillysillyman.springnewsfeed.follow;

import io.sillysillyman.springnewsfeed.common.Timestamped;
import io.sillysillyman.springnewsfeed.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(FollowId.class)
@Table(name = "follow")
public class Follow extends Timestamped {

    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "followee_id")
    private Long followeeId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    @JoinColumn(name = "followee_id", insertable = false, updatable = false)
    private User followee;

    public Follow(User follower, User followee) {
        this.followerId = follower.getId();
        this.followeeId = followee.getId();
        this.follower = follower;
        this.followee = followee;
    }
}