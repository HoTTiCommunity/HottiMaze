package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.QuestionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPostRepository extends JpaRepository<QuestionPost, Long> {

    Page<QuestionPost> findByMazeIdAndQuestionOrderOrderByCreatedAtDesc(
            Long mazeId, Integer questionOrder, Pageable pageable);

    Page<QuestionPost> findByMazeIdOrderByQuestionOrderAscCreatedAtDesc(
            Long mazeId, Pageable pageable);
}