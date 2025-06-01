package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.service.MazeService;
import com.example.HottiMaze.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;
    private final MazeService mazeService;

    @GetMapping("/")
    public String index(Model model) {
        List<PostDto> noticePosts = postService.getPostsByCategoryName("공지사항");
        List<PostDto> freePosts = postService.getPostsByCategoryName("자유게시판");

        if (noticePosts.size() > 5) {
            noticePosts = noticePosts.subList(0, 5);
        }
        if (freePosts.size() > 5) {
            freePosts = freePosts.subList(0, 5);
        }
        List<MazeDto> latestMazes = mazeService.getLatestMazes();
        List<MazeDto> popularMazes = mazeService.getPopularMazes();

        model.addAttribute("noticePosts", noticePosts);
        model.addAttribute("freePosts", freePosts);
        model.addAttribute("latestMazes", latestMazes);
        model.addAttribute("popularMazes", popularMazes);

        return "index";
    }
}