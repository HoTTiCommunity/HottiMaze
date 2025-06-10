package com.example.HottiMaze.controller;

import com.example.HottiMaze.enums.UserRole;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.PostService;
import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostService postService;
    private final MazeService mazeService;
    private final UserService userService;

    @GetMapping
    public String adminDashboard(Model model) {
        // 관리자 대시보드에 미로 승인 대기 통계 추가
        long pendingMazeCount = mazeService.getMazeCountByStatus(com.example.HottiMaze.enums.MazeStatus.PENDING);
        model.addAttribute("pendingMazeCount", pendingMazeCount);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagement(Model model) {
        // 사용자 관리 페이지
        return "admin/users";
    }

    @PostMapping("/users/{userId}/role")
    @ResponseBody
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId,
                                                 @RequestParam UserRole role) {
        try {
            userService.changeUserRole(userId, role);
            return ResponseEntity.ok("권한이 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("권한 변경에 실패했습니다.");
        }
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 삭제에 실패했습니다.");
        }
    }
}