package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MazeQuestionDto {
    private Long id;
    private Long mazeId;
    private String questionImage;    // 이미지 경로
    private String correctAnswer;    // 정답
    private Integer questionOrder;   // 문제 순서
    private String title;           // 문제 제목 추가
    private Integer points;         // 점수 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String hint;
}