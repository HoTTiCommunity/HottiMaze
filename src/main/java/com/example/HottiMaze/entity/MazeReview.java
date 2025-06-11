package com.example.HottiMaze.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "maze_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"maze_id", "user_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MazeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maze_id", nullable = false)
    private Maze maze;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", length = 1000, nullable = false)
    private String content; // 리뷰 내용

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = true; // 미로 완주 여부

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}