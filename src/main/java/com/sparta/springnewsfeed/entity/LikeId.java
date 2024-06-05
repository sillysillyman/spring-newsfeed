package com.sparta.springnewsfeed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    /*
     * user entity, post entity 구현 후
     * JoinColumn 수정하기
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "post_id", nullable = false)
    private Long postId;
}