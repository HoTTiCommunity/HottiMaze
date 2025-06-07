package com.example.HottiMaze.dto;

import com.example.HottiMaze.enums.MazeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MazeApprovalDto {
    private Long mazeId;
    private MazeStatus status;
    private String rejectionReason;
    private String adminComment;
}
