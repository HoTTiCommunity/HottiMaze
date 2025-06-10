package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeCreateDto;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mazes")
@RequiredArgsConstructor
public class MazeUploadController {

    private final MazeService mazeService;

    // 미궁 업로드 페이지 표시
    @GetMapping("/upload")
    public String showUploadPage(Model model) {
        model.addAttribute("mazeCreateDto", new MazeCreateDto());
        return "maze-upload";
    }

    // 미궁 업로드 처리
    @PostMapping("/upload")
    public String uploadMaze(@ModelAttribute MazeCreateDto mazeCreateDto,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        try {
            mazeCreateDto.setCreatorName(principal.getUsername());
            // 입력 검증
            if (mazeCreateDto.getMazeTitle() == null || mazeCreateDto.getMazeTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("미로 제목을 입력해주세요.");
            }

            if (mazeCreateDto.getCreatorName() == null || mazeCreateDto.getCreatorName().trim().isEmpty()) {
                throw new IllegalArgumentException("제작자 이름을 입력해주세요.");
            }

            if (mazeCreateDto.getMainImage() == null || mazeCreateDto.getMainImage().isEmpty()) {
                throw new IllegalArgumentException("메인 이미지를 업로드해주세요.");
            }

            if (mazeCreateDto.getQuestionImages() == null || mazeCreateDto.getQuestionImages().isEmpty()) {
                throw new IllegalArgumentException("최소 1개 이상의 문제 이미지를 업로드해주세요.");
            }

            // 미로 생성 (PENDING 상태로 저장됨)
            MazeDto createdMaze = mazeService.createMaze(mazeCreateDto, principal.getUsername());

            redirectAttributes.addFlashAttribute("success",
                    "미로가 성공적으로 업로드되었습니다! 관리자 승인 후 게시됩니다.");
            redirectAttributes.addFlashAttribute("info",
                    "업로드된 미로는 관리자의 검토를 거쳐 승인/거부가 결정됩니다. 승인까지 시간이 소요될 수 있습니다.");

            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/mazes/upload";
        }
    }

    // 미궁 삭제 (추가 기능)
    @PostMapping("/{mazeId}/delete")
    public String deleteMaze(@PathVariable Long mazeId, RedirectAttributes redirectAttributes) {
        try {
            mazeService.deleteMaze(mazeId);
            redirectAttributes.addFlashAttribute("success", "미로가 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "미로 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/mazes/" + mazeId;
        }
    }
}