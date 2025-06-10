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

    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @Column(name = "question_image", nullable = false)
    private String questionImage; // 이미지 경로

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer; // 정답 (복수 정답은 "|"로 구분: "test|테스트|정답")

    @Column(name = "question_order")
    private Integer questionOrder; // 문제 순서

    // 선택적 필드들
    @Column(name = "title")
    private String title; // 문제 제목

    @Column(name = "points")
    private Integer points = 10; // 획득 포인트 (기본값 10점)

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "hint", length = 500)
    private String hint; // 문제 힌트

}