package io.sillysillyman.springnewsfeed.like.postlike;

import io.sillysillyman.springnewsfeed.common.Timestamped;
import io.sillysillyman.springnewsfeed.post.Post;
import io.sillysillyman.springnewsfeed.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
