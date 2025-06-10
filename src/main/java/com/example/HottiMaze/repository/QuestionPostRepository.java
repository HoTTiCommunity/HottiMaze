package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.QuestionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPostRepository extends JpaRepository<QuestionPost, Long> {
    Page<QuestionPost> findAllByMgId(Long mgId, Pageable pageable);
}