package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionPostDto {
    private Long id;
    private Long mazeId;
    private Integer questionOrder;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdAt;
}