package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.MazeVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MazeVoteRepository extends JpaRepository<MazeVote, Long> {

    // 특정 사용자가 특정 미로에 투표했는지 확인
    Optional<MazeVote> findByMazeIdAndUserId(Long mazeId, Long userId);

    // 특정 미로의 좋아요 개수 조회
    @Query("SELECT COUNT(mv) FROM MazeVote mv WHERE mv.maze.id = :mazeId AND mv.isLike = true")
    long countLikesByMazeId(@Param("mazeId") Long mazeId);

    // 특정 미로의 싫어요 개수 조회
    @Query("SELECT COUNT(mv) FROM MazeVote mv WHERE mv.maze.id = :mazeId AND mv.isLike = false")
    long countDislikesByMazeId(@Param("mazeId") Long mazeId);

    // 특정 미로의 전체 투표 개수 조회
    long countByMazeId(Long mazeId);

    // 특정 사용자가 투표한 미로 개수
    long countByUserId(Long userId);

    // 미로 삭제 시 관련 투표 삭제를 위한 메서드
    void deleteByMazeId(Long mazeId);
}