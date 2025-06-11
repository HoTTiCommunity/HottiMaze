package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeUpdateDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserService userService;
    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;

    /**
     * 유저 상세보기 페이지
     */
    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal UserDetails principal,
                              Model model) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            String username = principal.getUsername();
            User user = userService.getUserByUsername(username);
            List<MazeDto> userMazes = mazeService.getUserMazes(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userMazes", userMazes);
            model.addAttribute("totalMazeCount", userMazes.size());

            long pendingCount = userMazes.stream()
                    .filter(maze -> "PENDING".equals(maze.getStatus().name()))
                    .count();
            long approvedCount = userMazes.stream()
                    .filter(maze -> "APPROVED".equals(maze.getStatus().name()))
                    .count();
            long rejectedCount = userMazes.stream()
                    .filter(maze -> "REJECTED".equals(maze.getStatus().name()))
                    .count();

            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("approvedCount", approvedCount);
            model.addAttribute("rejectedCount", rejectedCount);

            return "user-profile";

        } catch (Exception e) {
            model.addAttribute("error", "사용자 정보를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * 미로 수정 페이지
     */
    @GetMapping("/maze/{mazeId}/edit")
    public String editMazePage(@PathVariable Long mazeId,
                               @AuthenticationPrincipal UserDetails principal,
                               Model model) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }

            MazeDto maze = mazeService.getMaze(mazeId);

            if (!maze.getCreatorName().equals(principal.getUsername())) {
                model.addAttribute("error", "본인이 작성한 미로만 수정할 수 있습니다.");
                return "redirect:/user/profile";
            }

            // 승인된 미로는 수정 불가(일단 승인됐으면 고치지 말자)
            if ("APPROVED".equals(maze.getStatus().name())) {
                model.addAttribute("error", "승인된 미로는 수정할 수 없습니다.");
                return "redirect:/user/profile";
            }
            // 미로 문제들 조회
            List<MazeQuestionDto> mazeQuestions = mazeQuestionService.getMazeQuestions(mazeId);
            model.addAttribute("maze", maze);
            model.addAttribute("mazeQuestions", mazeQuestions);
            return "maze-edit";
        } catch (Exception e) {
            model.addAttribute("error", "미로 정보를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/user/profile";
        }
    }

    /**
     * 미로 수정 처리
     */
    @PostMapping("/maze/{mazeId}/edit")
    public String updateMaze(@PathVariable Long mazeId,
                             @ModelAttribute MazeUpdateDto updateDto,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            MazeDto maze = mazeService.getMaze(mazeId);

            if (!maze.getCreatorName().equals(principal.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "본인이 작성한 미로만 수정할 수 있습니다.");
                return "redirect:/user/profile";
            }

            // 승인된 미로는 수정 불가
            if ("APPROVED".equals(maze.getStatus().name())) {
                redirectAttributes.addFlashAttribute("error", "승인된 미로는 수정할 수 없습니다.");
                return "redirect:/user/profile";
            }
            // 입력 검증
            if (updateDto.getMazeTitle() == null || updateDto.getMazeTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("미로 제목을 입력해주세요.");
            }
            if (updateDto.getCorrectAnswers() == null || updateDto.getCorrectAnswers().isEmpty()) {
                throw new IllegalArgumentException("최소 1개 이상의 문제가 필요합니다.");
            }
            mazeService.updateMaze(mazeId, updateDto, principal.getUsername());
            redirectAttributes.addFlashAttribute("success", "미로가 성공적으로 수정되었습니다! 관리자 승인 후 게시됩니다.");
            return "redirect:/user/profile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "미로 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/user/maze/" + mazeId + "/edit";
        }
    }

    /**
     * 미로 삭제 처리
     */
    @PostMapping("/maze/{mazeId}/delete")
    public String deleteMaze(@PathVariable Long mazeId,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }

            MazeDto maze = mazeService.getMaze(mazeId);

            // 작성자 확인
            if (!maze.getCreatorName().equals(principal.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "본인이 작성한 미로만 삭제할 수 있습니다.");
                return "redirect:/user/profile";
            }
            mazeService.deleteMaze(mazeId);
            redirectAttributes.addFlashAttribute("success", "미로가 성공적으로 삭제되었습니다.");
            return "redirect:/user/profile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "미로 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/user/profile";
        }
    }
}