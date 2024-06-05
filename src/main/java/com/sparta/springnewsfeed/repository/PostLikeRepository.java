package com.sparta.springnewsfeed.repository;

import com.sparta.springnewsfeed.entity.LikeId;
import com.sparta.springnewsfeed.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, LikeId> {
    Optional<PostLike> findByIdUserIdAndIdPostId(Long userId, Long postId);
}
