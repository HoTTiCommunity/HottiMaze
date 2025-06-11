package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MazeReviewDto {
    private Long id;
    private Long mazeId;
    private String mazeTitle;
    private String username;
    private String userDisplayName;
    private String content;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isMyReview;
}