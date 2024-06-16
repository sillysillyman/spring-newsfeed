package io.sillysillyman.springnewsfeed.comment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByUpdatedAt();
}