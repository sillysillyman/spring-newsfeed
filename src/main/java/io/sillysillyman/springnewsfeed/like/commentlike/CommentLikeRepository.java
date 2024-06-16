package io.sillysillyman.springnewsfeed.like.commentlike;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

    Optional<CommentLike> findByIdUserIdAndIdCommentId(Long userId, Long commentId);

    boolean existsByIdUserIdAndIdCommentId(Long userId, Long commentId);
}
