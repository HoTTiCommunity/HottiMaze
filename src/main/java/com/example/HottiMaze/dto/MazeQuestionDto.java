package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MazeQuestionDto {
    private Long id;
    private Long mazeId;
    private String questionImage;
    private String correctAnswer;
    private Integer questionOrder;
    private String title;
    private Integer points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String hint;
}