package com.sparta.springnewsfeed.follow;

import com.sparta.springnewsfeed.nomal.Timestamped;
import com.sparta.springnewsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "follow")
public class Follow extends Timestamped {

    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "followed_id")
    private Long followedId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    @JoinColumn(name = "follower_id", insertable = false, updatable = false) // 충돌 방지
    private User follower; // 팔로워를 한 주체

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    @JoinColumn(name = "followed_id", insertable = false, updatable = false) // 충돌 방지
    private User followed; // 팔로우를 당한 대상자
}