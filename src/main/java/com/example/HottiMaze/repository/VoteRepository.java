package com.example.HottiMaze.repository;

import com.example.HottiMaze.entity.Post; // Post 엔티티 import
import com.example.HottiMaze.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostIdAndUsername(Long postId, String username);
    void deleteByPost(Post post);
}