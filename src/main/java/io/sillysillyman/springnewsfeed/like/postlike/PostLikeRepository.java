package io.sillysillyman.springnewsfeed.like.postlike;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

    Optional<PostLike> findByIdUserIdAndIdPostId(Long userId, Long postId);

    boolean existsByIdUserIdAndIdPostId(Long userId, Long postId);
}
