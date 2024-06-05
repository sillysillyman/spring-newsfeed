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
@Table(name = "commentLike")
public class CommentLike extends Timestamped {
    // 복합키 embedded: CommentLikeId
    @EmbeddedId
    //private CommentLikeId id;
    private LikeId id;
}