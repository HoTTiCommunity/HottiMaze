// src/main/java/com/example/HottiMaze/repository/MazeReviewRepository.java
package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.MazeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MazeReviewRepository extends JpaRepository<MazeReview, Long> {

    List<MazeReview> findByMazeIdOrderByCreatedAtDesc(Long mazeId);

    Optional<MazeReview> findByMazeIdAndUserId(Long mazeId, Long userId);

    long countByMazeId(Long mazeId);

    List<MazeReview> findByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByMazeId(Long mazeId);

    // New method to count completion records
    long countByMazeIdAndIsCompleted(Long mazeId, Boolean isCompleted); //
}