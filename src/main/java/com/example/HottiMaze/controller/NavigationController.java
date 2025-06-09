package com.example.HottiMaze.controller;

@GetMapping("/maze/{mazeId}/navigation")
public ResponseEntity<ProblemNavigationDto> getProblemNavigation(
        @PathVariable Long mazeId,
        @RequestParam Long userId) {
    ProblemNavigationDto dto = mazeService.getProblemNavigation(mazeId, userId);
    return ResponseEntity.ok(dto);
}
