package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.MazeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MazeQuestionRepository extends JpaRepository<MazeQuestion, Long> {
    List<MazeQuestion> findByMazeIdOrderByCreatedAtDesc(Long mazeId);
    List<MazeQuestion> findByMazeIdOrderByQuestionOrderAsc(Long mazeId);
}