package com.apiece.springboot_twitter.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 모든 댓글을 시간 순으로 가져오기
    List<Comment> findByPostIdOrderByIdDesc(Long postId);
    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);

    @EntityGraph(attributePaths = "post", type = EntityGraph.EntityGraphType.LOAD)
    List<Comment> findAll();
}
