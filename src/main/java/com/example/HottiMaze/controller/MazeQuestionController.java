package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeReviewService;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MazeQuestionController {

    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;
    private final MazeReviewService mazeReviewService; // 추가
    private final UserService userService; // 추가

    @GetMapping("/mazes/{mazeId}/quiz")
    public String startQuiz(@PathVariable Long mazeId, Model model) {
        try {
            System.out.println("퀴즈 요청 - 미로 ID: " + mazeId);

            MazeDto maze = mazeService.getMaze(mazeId);
            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            System.out.println("미로 정보: " + maze.getMazeTitle());
            System.out.println("문제 개수: " + questions.size());

            for (MazeQuestionDto q : questions) {
                System.out.println("순서: " + q.getQuestionOrder());
            }

            model.addAttribute("maze", maze);
            model.addAttribute("questions", questions);

            return "maze-quiz";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "미로 정보를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/mazes/" + mazeId;
        }
    }

    @PostMapping("/mazes/{mazeId}/quiz/submit")
    @ResponseBody
    public String submitQuiz(@PathVariable Long mazeId,
                             @RequestBody List<Integer> answers,
                             @AuthenticationPrincipal UserDetails principal) {
        try {
            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            int score = 0;
            for (int i = 0; i < Math.min(answers.size(), questions.size()); i++) {
                if (answers.get(i).equals(questions.get(i).getCorrectAnswer())) {
                    score++;
                }
            }

            // 모든 문제를 맞췄고 로그인한 사용자인 경우 완주 기록 생성
            if (score == questions.size() && principal != null) {
                String username = principal.getUsername();
                User user = userService.getUserByUsername(username);

                // 이미 완주 기록이 있는지 확인
                boolean hasCompleted = mazeReviewService.hasUserCompleted(mazeId, username);
                if (!hasCompleted) {
                    // 완주 기록 생성 (빈 리뷰로 완주 표시용)
                    mazeReviewService.createCompletionRecord(mazeId, username);
                    System.out.println("사용자 " + username + "가 미로 " + mazeId + " 완주 기록 생성");
                }
            }

            return "{\"score\": " + score + ", \"total\": " + questions.size() + "}";
        } catch (Exception e) {
            return "{\"error\": \"점수 계산 중 오류가 발생했습니다.\"}";
        }
    }
}