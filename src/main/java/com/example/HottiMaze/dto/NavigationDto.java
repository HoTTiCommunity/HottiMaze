package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
@AllArgsConstructor

public class NavigationDto {
    private Long mazeId;
    private List<Integer> solvedProblemNumbers;
    private Integer currentProblemNumber;
}