package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Maze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MazeRepository extends JpaRepository<Maze, Long> {

    @Query("SELECT m FROM Maze m ORDER BY m.createdAt DESC")
    List<Maze> findLatestMazes(Pageable pageable);

    @Query("SELECT m FROM Maze m ORDER BY m.viewCount DESC")
    List<Maze> findPopularMazes(Pageable pageable);

    List<Maze> findByUserIdOrderByCreatedAtDesc(Long userId);
}