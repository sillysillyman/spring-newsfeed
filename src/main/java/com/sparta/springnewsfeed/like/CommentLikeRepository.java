package com.sparta.springnewsfeed.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, LikeId> {
    Optional<CommentLike> findByIdUserIdAndIdPostId(Long userId, Long postId);
}
