// src/main/java/com/example/HottiMaze/controller/MainController.java
package com.example.HottiMaze.controller;

import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.PostService;
import com.example.HottiMaze.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final UserService userService;
    private final MazeService mazeService;

    @GetMapping("/")
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails principal) {

        // ─── 1) 로그인된 사용자가 있으면 User 엔티티 조회해서 모델에 담기 ───
        if (principal != null) {
            String username = principal.getUsername();
            User user = userService.getUserByUsername(username);

            model.addAttribute("loginUsername", user.getUsername());
            model.addAttribute("loginPoint", user.getPoint());
            model.addAttribute("loginChulcheckCount", user.getChulcheck());
            model.addAttribute("loginIsAvailableChulcheck", user.getIsAvailableChulcheck());
        }

        // ─── 2) 카테고리별 게시글 목록 조회 ───
        List<PostDto> noticePosts = postService.getPostsByCategoryName("공지사항");
        List<PostDto> freePosts = postService.getPostsByCategoryName("자유게시판");
        List<PostDto> qnaPosts = postService.getPostsByCategoryName("질문과답변");
        List<PostDto> confirmPosts = postService.getPostsByCategoryName("감옥 Confirm게시판");
        List<PostDto> jailPosts = postService.getPostsByCategoryName("감옥게시판");

        // 최대 5개까지만 표시
        if (noticePosts.size() > 5) {
            noticePosts = noticePosts.subList(0, 5);
        }
        if (freePosts.size() > 5) {
            freePosts = freePosts.subList(0, 5);
        }
        if (qnaPosts.size() > 5) {
            qnaPosts = qnaPosts.subList(0, 5);
        }
        if (confirmPosts.size() > 5) {
            confirmPosts = confirmPosts.subList(0, 5);
        }
        if (jailPosts.size() > 5) {
            jailPosts = jailPosts.subList(0, 5);
        }

        // ─── 3) 미로 목록 조회 ───
        List<MazeDto> latestMazes = mazeService.getLatestMazes();
        List<MazeDto> popularMazes = mazeService.getPopularMazes();

        // ─── 4) 모델에 데이터 추가 (중복 제거) ───
        // 게시글 데이터
        model.addAttribute("noticePosts", noticePosts);
        model.addAttribute("freePosts", freePosts);
        model.addAttribute("qnaPosts", qnaPosts);
        model.addAttribute("confirmPosts", confirmPosts);
        model.addAttribute("jailPosts", jailPosts);

        // 미로 데이터 (한 번만 추가)
        model.addAttribute("latestMazes", latestMazes);
        model.addAttribute("popularMazes", popularMazes);

        return "index";
    }
}