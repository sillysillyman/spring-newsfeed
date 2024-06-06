package com.sparta.springnewsfeed.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    Optional<PostLike> findByIdUserIdAndIdPostId(Long userId, Long postId);
}
