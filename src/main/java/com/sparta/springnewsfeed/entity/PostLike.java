package com.sparta.springnewsfeed.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "postLike")
public class PostLike extends Timestamped {
    // 복합키 embedded: PostLikeId
    @EmbeddedId
    //private PostLikeId id;
    private LikeId id;
}