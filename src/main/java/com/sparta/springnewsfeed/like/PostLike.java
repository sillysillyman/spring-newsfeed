package com.sparta.springnewsfeed.like;

import com.sparta.springnewsfeed.common.Timestamped;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_like")
public class PostLike extends Timestamped {
    @EmbeddedId
    private PostLikeId id;

    // Fetch Type 고민좀 해보기!! EAGER / LAZY
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
