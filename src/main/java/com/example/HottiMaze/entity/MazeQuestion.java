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
public class MazeQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "maze_id")
    private Maze maze;

    // 필수 필드들
    @Column(name = "question_image", nullable = false)
    private String questionImage; // 이미지 경로

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer; // 정답 (예: "test")

    @Column(name = "question_order")
    private Integer questionOrder; // 문제 순서

    // 선택적 필드들
    @Column(name = "title")
    private String title; // 문제 제목 (선택사항)

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}