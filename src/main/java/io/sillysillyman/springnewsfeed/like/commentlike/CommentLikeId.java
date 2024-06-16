package io.sillysillyman.springnewsfeed.like.commentlike;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// 복합키 식별자 클래스
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentLikeId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;
}
