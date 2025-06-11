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
     * 🔥 수정된 미로 완주 기록 생성 API
     * - 프론트엔드에서 모든 문제 완료 확인 후에만 호출되도록 보장
     * POST /api/mazes/{mazeId}/complete
     */
    @PostMapping("/api/mazes/{mazeId}/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeMaze(
            @PathVariable Long mazeId,
            @RequestParam(required = false) List<String> completedAnswers, // 선택적: 완료된 답변들
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
                // 📋 선택적 검증: 서버에서도 문제 개수 확인 (보안 강화)
                List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

                // 만약 완료된 답변이 전달되었다면, 문제 개수와 일치하는지 확인
                if (completedAnswers != null && !completedAnswers.isEmpty()) {
                    if (completedAnswers.size() != questions.size()) {
                        response.put("success", false);
                        response.put("message", "모든 문제를 완료해야 합니다.");
                        return ResponseEntity.badRequest().body(response);
                    }
                }

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
     * 🚫 기존 제출 메서드 - 더 이상 사용하지 않음 (보안상 문제가 있었음)
     * 프론트엔드에서 정답 검증을 하므로 서버 검증이 우회될 수 있음
     */
    @PostMapping("/mazes/{mazeId}/quiz/submit")
    @ResponseBody
    @Deprecated
    public String submitQuiz(@PathVariable Long mazeId,
                             @RequestBody List<String> answers, // Integer -> String으로 변경
                             @AuthenticationPrincipal UserDetails principal) {
        try {
            System.out.println("⚠️ 경고: submitQuiz는 더 이상 권장되지 않습니다. /api/mazes/{mazeId}/complete를 사용하세요.");

            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            // 간단한 검증만 수행 (정확한 검증은 프론트엔드에 위임)
            int correctCount = 0;
            for (int i = 0; i < Math.min(answers.size(), questions.size()); i++) {
                String userAnswer = answers.get(i);
                String correctAnswer = questions.get(i).getCorrectAnswer();

                // 복수 정답 처리
                String[] correctAnswers = correctAnswer.toLowerCase().split("\\|");
                for (String correct : correctAnswers) {
                    if (correct.trim().equals(userAnswer.toLowerCase().trim())) {
                        correctCount++;
                        break;
                    }
                }
            }

            // 모든 문제를 맞췄고 로그인한 사용자인 경우 완주 기록 생성
            if (correctCount == questions.size() && principal != null) {
                String username = principal.getUsername();

                // 이미 완주 기록이 있는지 확인
                boolean hasCompleted = mazeReviewService.hasUserCompleted(mazeId, username);
                if (!hasCompleted) {
                    // 완주 기록 생성
                    mazeReviewService.createCompletionRecord(mazeId, username);
                    System.out.println("사용자 " + username + "가 미로 " + mazeId + " 완주 기록 생성 (레거시 메서드)");
                }
            }

            return "{\"score\": " + correctCount + ", \"total\": " + questions.size() + "}";
        } catch (Exception e) {
            return "{\"error\": \"점수 계산 중 오류가 발생했습니다.\"}";
        }
    }
}