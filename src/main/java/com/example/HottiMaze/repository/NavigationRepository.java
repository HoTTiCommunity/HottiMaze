package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NavigationRepository extends JpaRepository<Navigation, Long> {
    @Query("SELECT p.number FROM Submission s JOIN s.problem p WHERE s.maze.id = :mazeId AND s.user.id = :userId AND s.correct = true")
    List<Integer> findSolvedProblemNumbers(Long mazeId, Long userId);

    @Query("SELECT p.number FROM Submission s JOIN s.problem p WHERE s.maze.id = :mazeId AND s.user.id = :userId ORDER BY s.timestamp DESC LIMIT 1")
    Integer findCurrentProblemNumber(Long mazeId, Long userId);
}
