package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.enums.MazeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MazeRepository extends JpaRepository<Maze, Long> {

    // 승인된 미로만 조회하도록 수정
    @Query("SELECT m FROM Maze m WHERE m.status = 'APPROVED' ORDER BY m.createdAt DESC")
    List<Maze> findLatestMazes(Pageable pageable);

    @Query("SELECT m FROM Maze m WHERE m.status = 'APPROVED' ORDER BY m.viewCount DESC")
    List<Maze> findPopularMazes(Pageable pageable);

    List<Maze> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 상태별 미로 조회
    List<Maze> findByStatusOrderByCreatedAtDesc(MazeStatus status);

    // 승인 대기 중인 미로 조회
    List<Maze> findByStatusOrderByCreatedAtAsc(MazeStatus status);

    // 승인된 미로만 조회
    List<Maze> findByStatusAndUserIdOrderByCreatedAtDesc(MazeStatus status, Long userId);

    // 상태별 개수 조회
    long countByStatus(MazeStatus status);
}