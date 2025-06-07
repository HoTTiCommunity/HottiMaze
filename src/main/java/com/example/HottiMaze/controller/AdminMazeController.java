package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeApprovalDto;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.enums.MazeStatus;
import com.example.HottiMaze.service.MazeService;
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
@RequestMapping("/admin/mazes")
@RequiredArgsConstructor
public class AdminMazeController {

    private final MazeService mazeService;

    // 미로 승인 관리 페이지
    @GetMapping("/approval")
    public String mazeApprovalPage(Model model) {
        List<MazeDto> pendingMazes = mazeService.getPendingMazes();
        List<MazeDto> allMazes = mazeService.getAllMazesForAdmin();

        // 상태별 통계
        long pendingCount = mazeService.getMazeCountByStatus(MazeStatus.PENDING);
        long approvedCount = mazeService.getMazeCountByStatus(MazeStatus.APPROVED);
        long rejectedCount = mazeService.getMazeCountByStatus(MazeStatus.REJECTED);

        model.addAttribute("pendingMazes", pendingMazes);
        model.addAttribute("allMazes", allMazes);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);

        return "admin/maze-approval";
    }

    // 미로 승인 API
    @PostMapping("/{mazeId}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveMaze(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            mazeService.approveMaze(mazeId, principal.getUsername());
            response.put("success", true);
            response.put("message", "미로가 성공적으로 승인되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "미로 승인 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 미로 거부 API - autoDelete 파라미터 추가
    @PostMapping("/{mazeId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectMaze(
            @PathVariable Long mazeId,
            @RequestParam String rejectionReason,
            @RequestParam(defaultValue = "false") boolean autoDelete,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "거부 사유를 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            // autoDelete 파라미터를 서비스 메서드에 전달
            mazeService.rejectMaze(mazeId, rejectionReason, principal.getUsername(), autoDelete);

            String successMessage = autoDelete
                    ? "미로가 거부되고 관련 파일이 삭제되었습니다."
                    : "미로가 거부되었습니다.";

            response.put("success", true);
            response.put("message", successMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "미로 거부 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 거부된 미로 삭제 API (기존 거부된 미로를 나중에 삭제하는 용도)
    @DeleteMapping("/{mazeId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRejectedMaze(
            @PathVariable Long mazeId,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 미로 정보 먼저 확인
            MazeDto maze = mazeService.getMaze(mazeId);

            if (maze.getStatus() != MazeStatus.REJECTED) {
                response.put("success", false);
                response.put("message", "거부된 미로만 삭제할 수 있습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 미로와 관련 파일 완전 삭제
            mazeService.deleteMaze(mazeId);

            response.put("success", true);
            response.put("message", "거부된 미로가 완전히 삭제되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "미로 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 일괄 처리 API - autoDelete 지원
    @PostMapping("/batch-process")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> batchProcessMazes(
            @RequestBody List<MazeApprovalDto> approvals,
            @AuthenticationPrincipal UserDetails principal) {

        Map<String, Object> response = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        for (MazeApprovalDto approval : approvals) {
            try {
                if (approval.getStatus() == MazeStatus.REJECTED) {
                    // 일괄 거부 시에는 기본적으로 파일을 삭제하지 않음
                    // (개별 처리에서만 autoDelete 옵션 제공)
                    mazeService.rejectMaze(
                            approval.getMazeId(),
                            approval.getRejectionReason(),
                            principal.getUsername(),
                            false // 일괄 처리 시에는 자동 삭제 안 함
                    );
                } else {
                    mazeService.processMazeApproval(approval, principal.getUsername());
                }
                successCount++;
            } catch (Exception e) {
                failCount++;
                System.err.println("미로 처리 실패 (ID: " + approval.getMazeId() + "): " + e.getMessage());
            }
        }

        response.put("success", true);
        response.put("message", String.format("처리 완료: 성공 %d건, 실패 %d건", successCount, failCount));
        response.put("successCount", successCount);
        response.put("failCount", failCount);

        return ResponseEntity.ok(response);
    }

    // 미로 상세 정보 API (관리자용)
    @GetMapping("/{mazeId}/details")
    @ResponseBody
    public ResponseEntity<MazeDto> getMazeDetails(@PathVariable Long mazeId) {
        try {
            MazeDto maze = mazeService.getMaze(mazeId);
            return ResponseEntity.ok(maze);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 승인 대기 중인 미로 목록 API
    @GetMapping("/pending")
    @ResponseBody
    public ResponseEntity<List<MazeDto>> getPendingMazes() {
        List<MazeDto> pendingMazes = mazeService.getPendingMazes();
        return ResponseEntity.ok(pendingMazes);
    }
}