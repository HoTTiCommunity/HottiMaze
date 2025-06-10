package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.enums.MazeStatus;
import com.example.HottiMaze.service.MazeQuestionService;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mazes")
@RequiredArgsConstructor
public class MazeController {
    private final MazeService mazeService;
    private final MazeQuestionService mazeQuestionService;

    @GetMapping("/{mazeId}")
    public String getMaze(@PathVariable Long mazeId, Model model, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("미로 상세보기 요청 - ID: " + mazeId);

            // 미로 정보 조회
            MazeDto mazeDto = mazeService.getMazeAndIncreaseViewCount(mazeId);

            if (mazeDto == null) {
                System.err.println("미로를 찾을 수 없음 - ID: " + mazeId);
                redirectAttributes.addFlashAttribute("error", "존재하지 않는 미로입니다.");
                return "redirect:/";
            }

            System.out.println("미로 조회 성공: " + mazeDto.getMazeTitle());
            System.out.println("미로 상태: " + mazeDto.getStatus());

            // 승인되지 않은 미로 접근 권한 확인
            if (mazeDto.getStatus() != MazeStatus.APPROVED) {
                String currentUsername = SecurityUtils.getCurrentUsername();
                boolean isAdmin = SecurityUtils.isAdmin();
                boolean isOwner = currentUsername != null && currentUsername.equals(mazeDto.getCreatorName());

                System.out.println("미승인 미로 접근 시도 - 사용자: " + currentUsername + ", 관리자: " + isAdmin + ", 소유자: " + isOwner);

                if (!isAdmin && !isOwner) {
                    redirectAttributes.addFlashAttribute("error", "승인되지 않은 미로입니다.");
                    return "redirect:/";
                }

                // 상태 메시지 설정
                if (mazeDto.getStatus() == MazeStatus.PENDING) {
                    model.addAttribute("statusMessage", "이 미로는 현재 관리자 승인을 기다리고 있습니다.");
                } else if (mazeDto.getStatus() == MazeStatus.REJECTED) {
                    model.addAttribute("statusMessage", "이 미로는 거부되었습니다: " + mazeDto.getRejectionReason());
                }
            }

            // 미로 문제들 조회
            List<MazeQuestionDto> mazeQuestionDto = mazeQuestionService.getMazeQuestions(mazeId);
            System.out.println("미로 문제 개수: " + mazeQuestionDto.size());

            // 모델에 데이터 추가
            model.addAttribute("maze", mazeDto);
            model.addAttribute("mazeQuestions", mazeQuestionDto);

            System.out.println("모델에 데이터 추가 완료");
            return "maze-detail";

        } catch (IllegalArgumentException e) {
            System.err.println("미로 조회 실패 - 잘못된 인수: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 미로입니다.");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("미로 조회 중 예상치 못한 오류: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "미로를 불러올 수 없습니다: " + e.getMessage());
            return "redirect:/";
        }
    }
}