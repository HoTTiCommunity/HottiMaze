package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MazeVoteDto {
    private Long mazeId;
    private Boolean isLike; // true: 좋아요, false: 싫어요
}