package com.sparta.springnewsfeed.repository;

import com.sparta.springnewsfeed.entity.CommentLike;
import com.sparta.springnewsfeed.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, LikeId> {
    Optional<CommentLike> findByIdUserIdAndIdPostId(Long userId, Long postId);
}
