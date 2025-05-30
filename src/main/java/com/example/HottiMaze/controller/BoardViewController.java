package com.example.HottiMaze.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardViewController {
    @GetMapping
    public String boardList() {
        return "";
    }
    @GetMapping("/{postId}")
    public String postDetail(@PathVariable Long postId) {
        return "";
    }
    @GetMapping("/create")
    public String createPost() {
        return "";
    }
    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId) {
        return "";
    }
}
