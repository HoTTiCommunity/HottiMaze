package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MazeQuestionController {

    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;

    @GetMapping("/mazes/{mazeId}/quiz")
    public String startQuiz(@PathVariable Long mazeId, Model model) {
        try {
            System.out.println("퀴즈 요청 - 미로 ID: " + mazeId); // 디버깅용

            MazeDto maze = mazeService.getMaze(mazeId);
            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            System.out.println("미로 정보: " + maze.getMazeTitle()); // 디버깅용
            System.out.println("문제 개수: " + questions.size()); // 디버깅용

            for (MazeQuestionDto q : questions) {
                System.out.println("문제: " + q.getTitle() + ", 순서: " + q.getQuestionOrder());
            }

            model.addAttribute("maze", maze);
            model.addAttribute("questions", questions);

            return "maze-quiz";
        } catch (Exception e) {
            e.printStackTrace(); // 에러 출력
            model.addAttribute("error", "미로 정보를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/mazes/" + mazeId;
        }
    }

    @PostMapping("/mazes/{mazeId}/quiz/submit")
    @ResponseBody
    public String submitQuiz(@PathVariable Long mazeId,
                             @RequestBody List<Integer> answers) {
        try {
            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            int score = 0;
            for (int i = 0; i < Math.min(answers.size(), questions.size()); i++) {
                if (answers.get(i).equals(questions.get(i).getCorrectAnswer())) {
                    score++;
                }
            }

            return "{\"score\": " + score + ", \"total\": " + questions.size() + "}";
        } catch (Exception e) {
            return "{\"error\": \"점수 계산 중 오류가 발생했습니다.\"}";
        }
    }
}