package com.sparta.springnewsfeed.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, LikeId> {
    Optional<PostLike> findByIdUserIdAndIdPostId(Long userId, Long postId);
}
