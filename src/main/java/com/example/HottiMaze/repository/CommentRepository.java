package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 Post에 속한 모든 Comment를 최신순으로 조회
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    // 특정 Post의 특정 Comment를 조회 (댓글 수정/삭제 시 권한 확인용)
    Optional<Comment> findByIdAndPostId(Long id, Long postId);

    // 특정 Post의 댓글 개수 조회
    long countByPostId(Long postId);

    // 특정 User가 작성한 Comment 조회 (필요 시)
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 게시글 삭제 시 댓글 전체 삭제 (PostService에서 사용)
    void deleteByPostId(Long postId);
}