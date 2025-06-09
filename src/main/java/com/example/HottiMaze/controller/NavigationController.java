package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.NavigationDto;
import com.example.HottiMaze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NavigationController {
    private final MazeService mazeService;

    @GetMapping("/maze/{mazeId}/navigation")
    public ResponseEntity<NavigationDto> getProblemNavigation(
            @PathVariable Long mazeId,
            @RequestParam Long userId) {
        NavigationDto dto = mazeService.getProblemNavigation(mazeId, userId);
        return ResponseEntity.ok(dto);
    }
}