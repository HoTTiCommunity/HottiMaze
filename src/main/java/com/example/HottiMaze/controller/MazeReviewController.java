package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeReviewCreateDto;
import com.example.HottiMaze.dto.MazeReviewDto;
import com.example.HottiMaze.service.MazeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mazes")
@RequiredArgsConstructor
public class MazeReviewController {

    private final MazeReviewService mazeReviewService;

    /**
     * 미로 리뷰 작성
     */
    @PostMapping("/{mazeId}/reviews")
    public ResponseEntity<Map<String, Object>> createReview(
            @PathVariable Long mazeId,
            @RequestBody MazeReviewCreateDto createDto,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "리뷰를 작성하려면 로그인이 필요합니다."));
        }

        try {
            createDto.setMazeId(mazeId);
            MazeReviewDto review = mazeReviewService.createReview(principal.getUsername(), createDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 성공적으로 작성되었습니다.");
            response.put("review", review);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 미로의 모든 리뷰 조회
     */
    @GetMapping("/{mazeId}/reviews")
    public ResponseEntity<List<MazeReviewDto>> getMazeReviews(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        try {
            String username = principal != null ? principal.getUsername() : null;
            List<MazeReviewDto> reviews = mazeReviewService.getMazeReviews(mazeId, username);
            return ResponseEntity.ok(reviews);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 미로 리뷰 개수 조회
     */
    @GetMapping("/{mazeId}/review-count")
    public ResponseEntity<Map<String, Object>> getMazeReviewCount(@PathVariable Long mazeId) {
        try {
            long count = mazeReviewService.getMazeReviewCount(mazeId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 사용자의 리뷰 작성 여부 확인
     */
    @GetMapping("/{mazeId}/my-review-status")
    public ResponseEntity<Map<String, Object>> getMyReviewStatus(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            boolean hasReviewed = mazeReviewService.hasUserReviewed(mazeId, principal.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasReviewed", hasReviewed);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody MazeReviewCreateDto updateDto,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            MazeReviewDto review = mazeReviewService.updateReview(reviewId, principal.getUsername(), updateDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 성공적으로 수정되었습니다.");
            response.put("review", review);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Map<String, Object>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        try {
            mazeReviewService.deleteReview(reviewId, principal.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "리뷰가 삭제되었습니다.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}