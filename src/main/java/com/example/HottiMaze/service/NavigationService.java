package com.example.HottiMaze.service;

public ProblemNavigationDto getProblemNavigation(Long mazeId, Long userId) {
    List<Integer> solved = submissionRepository.findSolvedProblemNumbers(mazeId, userId);
    Integer current = submissionRepository.findCurrentProblemNumber(mazeId, userId);

    return new ProblemNavigationDto(mazeId, solved, current);
}
