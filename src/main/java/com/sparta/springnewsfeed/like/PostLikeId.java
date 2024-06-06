package com.sparta.springnewsfeed.like;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 복합키 식별자 클래스
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;
}
