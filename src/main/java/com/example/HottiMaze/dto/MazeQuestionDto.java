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
    private String mazeTitle;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 퀴즈를 위한 추가 필드들
    private String questionImage;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Integer correctAnswer;
    private Integer questionOrder;
}