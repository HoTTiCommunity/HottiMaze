package com.example.HottiMaze.repository;

public interface NavigationRepository {
    @Query("SELECT p.number FROM Submission s JOIN s.problem p WHERE s.maze.id = :mazeId AND s.user.id = :userId AND s.correct = true")
    List<Integer> findSolvedProblemNumbers(Long mazeId, Long userId);

    @Query("SELECT p.number FROM Submission s JOIN s.problem p WHERE s.maze.id = :mazeId AND s.user.id = :userId ORDER BY s.timestamp DESC LIMIT 1")
    Integer findCurrentProblemNumber(Long mazeId, Long userId);

}
