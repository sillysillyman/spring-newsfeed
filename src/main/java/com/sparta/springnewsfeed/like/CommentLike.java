package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.common.Timestamped;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
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
    @EmbeddedId
    private LikeId id;
}