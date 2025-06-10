package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface NavigationRepository extends JpaRepository<Navigation, Long> {

    // 정답으로 푼 문제 순서 목록
    @Query("SELECT n.mazeQuestion.questionOrder FROM Navigation n WHERE n.maze.id = :mazeId AND n.user.id = :userId AND n.correct = true")
    List<Integer> findSolvedProblemOrders(@Param("mazeId") Long mazeId, @Param("userId") Long userId);

    // 가장 최근에 푼 문제 순서 (여러 개 중 첫 번째를 꺼내야 함)
    @Query("SELECT n.mazeQuestion.questionOrder FROM Navigation n WHERE n.maze.id = :mazeId AND n.user.id = :userId ORDER BY n.timestamp DESC")
    List<Integer> findCurrentProblemOrder(@Param("mazeId") Long mazeId, @Param("userId") Long userId);
}
