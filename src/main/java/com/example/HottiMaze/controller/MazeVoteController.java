package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeVoteDto;
import com.example.HottiMaze.dto.MazeVoteStatsDto;
import com.example.HottiMaze.service.MazeVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mazes")
@RequiredArgsConstructor
public class MazeVoteController {

    private final MazeVoteService mazeVoteService;

    /**
     * 미로에 좋아요 투표
     * POST /api/mazes/{mazeId}/like
     */
    @PostMapping("/{mazeId}/like")
    public ResponseEntity<Map<String, Object>> likeMaze(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            MazeVoteDto voteDto = new MazeVoteDto(mazeId, true);
            MazeVoteStatsDto stats = mazeVoteService.voteMaze(principal.getUsername(), voteDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "투표가 완료되었습니다.");
            response.put("stats", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 미로에 싫어요 투표
     * POST /api/mazes/{mazeId}/dislike
     */
    @PostMapping("/{mazeId}/dislike")
    public ResponseEntity<Map<String, Object>> dislikeMaze(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            MazeVoteDto voteDto = new MazeVoteDto(mazeId, false);
            MazeVoteStatsDto stats = mazeVoteService.voteMaze(principal.getUsername(), voteDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "투표가 완료되었습니다.");
            response.put("stats", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 미로 투표 통계 조회
     * GET /api/mazes/{mazeId}/vote-stats
     */
    @GetMapping("/{mazeId}/vote-stats")
    public ResponseEntity<MazeVoteStatsDto> getMazeVoteStats(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        try {
            String username = principal != null ? principal.getUsername() : null;
            MazeVoteStatsDto stats = mazeVoteService.getMazeVoteStats(mazeId, username);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 투표 취소
     * DELETE /api/mazes/{mazeId}/vote
     */
    @DeleteMapping("/{mazeId}/vote")
    public ResponseEntity<Map<String, Object>> cancelVote(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            MazeVoteStatsDto stats = mazeVoteService.cancelVote(mazeId, principal.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "투표가 취소되었습니다.");
            response.put("stats", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 사용자의 투표 상태 조회
     * GET /api/mazes/{mazeId}/my-vote
     */
    @GetMapping("/{mazeId}/my-vote")
    public ResponseEntity<Map<String, Object>> getMyVote(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            Boolean voteStatus = mazeVoteService.getUserVoteStatus(mazeId, principal.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userVote", voteStatus);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}