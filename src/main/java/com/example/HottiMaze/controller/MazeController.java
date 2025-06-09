package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.enums.MazeStatus;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.utils.SecurityUtils;
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
        try {
            MazeDto mazeDto = mazeService.getMazeAndIncreaseViewCount(mazeId);

            if (mazeDto.getStatus() != MazeStatus.APPROVED) {
                String currentUsername = SecurityUtils.getCurrentUsername();
                boolean isAdmin = SecurityUtils.isAdmin();
                boolean isOwner = mazeDto.getCreatorName().equals(currentUsername);

                if (!isAdmin && !isOwner) {
                    model.addAttribute("error", "승인되지 않은 미로입니다.");
                    return "redirect:/";
                }
                if (mazeDto.getStatus() == MazeStatus.PENDING) {
                    model.addAttribute("statusMessage", "이 미로는 현재 관리자 승인을 기다리고 있습니다.");
                } else if (mazeDto.getStatus() == MazeStatus.REJECTED) {
                    model.addAttribute("statusMessage", "이 미로는 거부되었습니다: " + mazeDto.getRejectionReason());
                }
            }

            List<MazeQuestionDto> mazeQuestionDto = mazeQuestionService.getMazeQuestions(mazeId);
            model.addAttribute("maze", mazeDto);
            model.addAttribute("mazeQuestions", mazeQuestionDto);

            return "maze-detail";

        } catch (Exception e) {
            model.addAttribute("error", "미로를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/";
        }
    }
}