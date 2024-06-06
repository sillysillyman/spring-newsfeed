package com.sparta.springnewsfeed.follow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

// 복합키 만드는 클래스
@Getter
@Setter
@NoArgsConstructor
public class FollowId implements Serializable {
    private Long follower_id;
    private Long followed_id;

    public FollowId(Long follower_id, Long followed_id) {
        this.follower_id = follower_id;
        this.followed_id = followed_id;
    }

    //  hashCode()전 복합키는 단순 두외래키필드를 가진상태일뿐이니 그두개의 외래키를 제대로 가지고있는지 확인
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(follower_id, followId.follower_id) &&
                Objects.equals(followed_id, followId.followed_id);
    }

    // 확인이후 복합키를 hashCode()를 사용하여 암호화
    @Override
    public int hashCode() {
        return Objects.hash(follower_id, followed_id);
    }
}