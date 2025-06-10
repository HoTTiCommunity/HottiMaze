package com.example.HottiMaze.entity;

import com.example.HottiMaze.enums.MazeStatus;
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

    // 승인 상태 필드 추가
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MazeStatus status = MazeStatus.PENDING;

    // 승인/거부 관련 필드
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
}