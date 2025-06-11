package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.CommentCreateDto;
import com.example.HottiMaze.dto.CommentDto;
import com.example.HottiMaze.dto.CommentUpdateDto;
import com.example.HottiMaze.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     * POST /api/posts/{postId}/comments
     * @param postId 게시글 ID
     * @param createDto 댓글 내용 (content)
     * @param principal 현재 로그인 사용자 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateDto createDto,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "댓글을 작성하려면 로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            createDto.setPostId(postId); // URL 경로의 postId를 DTO에 설정
            CommentDto comment = commentService.createComment(principal.getUsername(), createDto);
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 작성되었습니다.");
            response.put("comment", comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "댓글 작성 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 특정 게시글의 모든 댓글 조회
     * GET /api/posts/{postId}/comments
     * @param postId 게시글 ID
     * @param principal 현재 로그인 사용자 정보 (선택 사항, 내 댓글 여부 확인용)
     * @return 댓글 목록
     */
    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails principal) {
        String username = principal != null ? principal.getUsername() : null;
        List<CommentDto> comments = commentService.getCommentsByPostId(postId, username);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 수정
     * PUT /api/posts/{postId}/comments/{commentId}
     * @param postId 게시글 ID
     * @param commentId 댓글 ID
     * @param updateDto 수정할 댓글 내용 (content)
     * @param principal 현재 로그인 사용자 정보
     * @return 수정된 댓글 정보
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateDto updateDto,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "댓글을 수정하려면 로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // postId는 경로 검증용으로 사용하고, 실제 업데이트는 commentId로 진행
            CommentDto updatedComment = commentService.updateComment(commentId, principal.getUsername(), updateDto);
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 수정되었습니다.");
            response.put("comment", updatedComment);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "댓글 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 댓글 삭제
     * DELETE /api/posts/{postId}/comments/{commentId}
     * @param postId 게시글 ID
     * @param commentId 댓글 ID
     * @param principal 현재 로그인 사용자 정보
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "댓글을 삭제하려면 로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // postId는 경로 검증용으로 사용하고, 실제 삭제는 commentId로 진행
            commentService.deleteComment(commentId, principal.getUsername());
            response.put("success", true);
            response.put("message", "댓글이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "댓글 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 특정 게시글의 댓글 개수 조회
     * GET /api/posts/{postId}/comments/count
     * @param postId 게시글 ID
     * @return 댓글 개수 정보
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCommentCount(@PathVariable Long postId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = commentService.getCommentCountByPostId(postId);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "댓글 개수 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}