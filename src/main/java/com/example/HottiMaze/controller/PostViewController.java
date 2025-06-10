package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.PostCreateDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.dto.PostUpdateDto;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.security.Principal;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;

    // 게시글 목록 (전체)
    @GetMapping
    public String boardList(Model model, Principal principal) {
        List<PostDto> allPosts = postService.getAllPosts();
        model.addAttribute("posts", allPosts);
        model.addAttribute("categoryName", null);

        // 로그인 여부
        boolean isLoggedIn = principal != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "post-list";
    }

    // 게시글 상세보기
    @GetMapping("/{postId}")
    public String postDetail(@PathVariable Long postId, Model model) {
        PostDto post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "geul";
    }

    // 게시글 생성 폼 표시
    @GetMapping("/create")
    public String createPost(Model model) {
        List<Category> categories = postService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postCreateDto", new PostCreateDto());
        return "post-create";
    }

    // 게시글 생성 처리
    @PostMapping("/create")
    public String createPost(@ModelAttribute PostCreateDto postCreateDto,
                             RedirectAttributes redirectAttributes) {
        try {
            PostDto createdPost = postService.createPost(postCreateDto);
            redirectAttributes.addFlashAttribute("success", "게시글이 성공적으로 작성되었습니다.");
            return "redirect:/post/" + createdPost.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/post/create";
        }
    }

    // 게시글 수정 폼 표시
    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId, Model model) {
        Post postEntity = postService.getPostEntityById(postId);

        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setTitle(postEntity.getTitle());
        postUpdateDto.setContent(postEntity.getContent());
        postUpdateDto.setCategoryId(postEntity.getCategory() != null ? postEntity.getCategory().getId() : null);
        postUpdateDto.setUpdatedBy(postEntity.getAuthor());

        List<Category> categories = postService.getAllCategories();

        model.addAttribute("postUpdateDto", postUpdateDto);
        model.addAttribute("categories", categories);
        model.addAttribute("postId", postId);

        return "post-edit";
    }

    // 게시글 수정 처리
    @PostMapping("/edit/{postId}")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostUpdateDto postUpdateDto,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.updatePost(postId, postUpdateDto);
            redirectAttributes.addFlashAttribute("success", "게시글이 성공적으로 수정되었습니다.");
            return "redirect:/post/" + postId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/post/edit/" + postId;
        }
    }

    // 게시글 삭제
    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId, RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(postId);
            redirectAttributes.addFlashAttribute("success", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/post";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/post/" + postId;
        }
    }

    // 카테고리별 게시글 목록
    @GetMapping("/name/{categoryName}")
    public String boardListByCategoryName(@PathVariable String categoryName, Model model) {
        try {
            List<PostDto> posts = postService.getPostsByCategoryName(categoryName);
            model.addAttribute("posts", posts);
            model.addAttribute("categoryName", categoryName);
            return "post-list";
        } catch (Exception e) {
            model.addAttribute("error", "카테고리를 찾을 수 없습니다: " + categoryName);
            return "redirect:/post";
        }
    }
    // 북마크 페이지
    @GetMapping("/bookmarks")
    public String bookmarks(Model m) {
        m.addAttribute("posts", postService.getAllPosts());
        return "bookmarks";
    }
}