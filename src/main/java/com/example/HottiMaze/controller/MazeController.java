package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mazes")
@RequiredArgsConstructor
public class MazeController {
    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;

    @GetMapping("/{mazeId}")
    public String getMaze(@PathVariable Long mazeId, Model model) {
        MazeDto mazeDto = mazeService.getMaze(mazeId);
        model.addAttribute("maze", mazeDto);
        List<MazeQuestionDto> mazeQuestionDto = mazeQuestionService.getMazeQuestions(mazeId);
        model.addAttribute("maze", mazeDto);
        model.addAttribute("mazeQuestions", mazeQuestionDto);
        return "maze-detail";
    }
}
