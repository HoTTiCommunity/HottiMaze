package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeReviewService;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MazeQuestionController {

    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;
    private final MazeReviewService mazeReviewService;
    private final UserService userService;

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

    /**
     * 미로 완주 기록 생성 API - 새로 추가
     * POST /api/mazes/{mazeId}/complete
     */
    @PostMapping("/api/mazes/{mazeId}/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeMaze(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        try {
            String username = principal.getUsername();

            // 이미 완주 기록이 있는지 확인
            boolean hasCompleted = mazeReviewService.hasUserCompleted(mazeId, username);

            if (!hasCompleted) {
                // 완주 기록 생성
                mazeReviewService.createCompletionRecord(mazeId, username);
                System.out.println("사용자 " + username + "가 미로 " + mazeId + " 완주 기록 생성");

                response.put("success", true);
                response.put("message", "미로 완주 기록이 생성되었습니다! 이제 리뷰를 작성할 수 있습니다.");
                response.put("newCompletion", true);
            } else {
                response.put("success", true);
                response.put("message", "이미 완주 기록이 있습니다.");
                response.put("newCompletion", false);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("완주 기록 생성 실패: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "완주 기록 생성 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 기존 메서드 - 사용하지 않으므로 제거하거나 주석 처리
     */
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