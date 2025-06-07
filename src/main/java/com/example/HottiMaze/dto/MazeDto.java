package com.example.HottiMaze.dto;

import com.example.HottiMaze.enums.MazeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MazeDto {
    private Long id;
    private String mazeTitle;
    private String mazeDir;
    private String creatorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
    private MazeStatus status;
    private LocalDateTime approvedAt;
    private String approvedByUsername;
    private String rejectionReason;
}