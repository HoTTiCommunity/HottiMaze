package com.example.HottiMaze.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Maze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maze_dir", nullable = false)

    @Column(name = "maze_id")
    private Long id;

    @Column(name = "maze_dir", nullable = true) // nullable = false에서 true로 변경

    private String mazeDir;

    @Column(name = "maze_title", nullable = false)
    private String mazeTitle;

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