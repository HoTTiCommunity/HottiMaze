package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.PostCreateDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;

    @GetMapping
    public String boardList() {
        return "";
    }

    @GetMapping("/{postId}")
    public String postDetail(@PathVariable Long postId) {
        return "";
    }

    @GetMapping("/create")
    public String createPost(Model model) {
        List<Category> categories = postService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postCreateDto", new PostCreateDto());
        return "post-create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostCreateDto postCreateDto) {
            PostDto createdPost = postService.createPost(postCreateDto);
            return "redirect:/";
    }
    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId) {
        return "";
    }
}
