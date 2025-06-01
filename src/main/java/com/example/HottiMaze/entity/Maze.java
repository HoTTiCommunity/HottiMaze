package com.example.HottiMaze.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Maze {
    @Id
    private Long id;
    @Column(name = "MazeDir", nullable = false)
    private String MazeDir;
    @Column(name = "MazeTitle", nullable = false)
    private String MazeTitle;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "view_count")
    private Integer viewCount = 0;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
