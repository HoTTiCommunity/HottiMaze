package com.example.HottiMaze.controller;

import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.UserRepository;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.service.MazeQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maze-hints")
@RequiredArgsConstructor
public class MazeHintController {
    private final UserRepository userRepository;
    private final MazeQuestionService mazeQuestionService;

    @PostMapping("/{mazeId}/{questionOrder}")
    @Transactional
    public ResponseEntity<Map<String, Object>> getHint(
            @PathVariable Long mazeId,
            @PathVariable Integer questionOrder, // Long → Integer로 변경
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "힌트를 보려면 로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            if (user.getPoint() < 50) {
                response.put("success", false);
                response.put("message", "포인트가 부족합니다. 힌트를 보려면 50포인트가 필요합니다."); // 오타 수정: messge → message
                response.put("userPoints", user.getPoint());
                return ResponseEntity.badRequest().body(response);
            }

            List<MazeQuestionDto> questions = mazeQuestionService.getMazeQuestions(mazeId);

            MazeQuestionDto targetQuestion = questions.stream()
                    .filter(q -> q.getQuestionOrder().equals(questionOrder))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("해당 문제를 찾을 수 없습니다."));

            if (targetQuestion.getHint() == null || targetQuestion.getHint().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "이 문제에는 힌트가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            user.setPoint(user.getPoint() - 50);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "힌트를 확인했습니다. (50포인트 소모)");
            response.put("hint", targetQuestion.getHint());
            response.put("userPoints", user.getPoint());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "힌트를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
