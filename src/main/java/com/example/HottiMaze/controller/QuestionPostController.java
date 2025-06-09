package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.QuestionPostDto;
import com.example.HottiMaze.service.QuestionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionPostController {

    private final QuestionPostService questionPostService;

    @GetMapping
    public String list(
            @RequestParam Long mgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionPostDto> paged = questionPostService.getPostsByMgId(mgId, pageable);
        List<QuestionPostDto> posts = paged.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("mgId", mgId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paged.getTotalPages());
        return "migung/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        QuestionPostDto post = questionPostService.getPost(id);
        model.addAttribute("post", post);
        return "migung/detail";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam Long mgId, Model model) {
        model.addAttribute("post", new QuestionPostDto());
        model.addAttribute("mgId", mgId);
        return "migung/form";
    }

    @PostMapping
    public String create(
            @ModelAttribute QuestionPostDto dto,
            @RequestParam Long mgId,
            Principal principal
    ) {
        dto.setMgId(mgId);
        dto.setAuthor(principal.getName());
        QuestionPostDto saved = questionPostService.createPost(dto);
        return "redirect:/questions?mgId=" + saved.getMgId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        QuestionPostDto post = questionPostService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("mgId", post.getMgId());
        return "migung/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute QuestionPostDto dto) {

        questionPostService.updatePost(id, dto);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        QuestionPostDto post = questionPostService.getPost(id);
        questionPostService.deletePost(id);
        return "redirect:/questions?mgId=" + post.getMgId();
    }

    @GetMapping("/maze-questionPost")
    public String mazeQuestionPost(@RequestParam Long mgId) {
        return "redirect:/questions?mgId=" + mgId;
    }
}
