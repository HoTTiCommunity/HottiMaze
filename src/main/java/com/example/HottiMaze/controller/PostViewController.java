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

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;
    // 게시글 생성
    @GetMapping
    public String boardList() {
        return "";
    }

    @GetMapping("/{postId}")
    public String postDetail(@PathVariable Long postId, Model model) {
        PostDto post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "geul";
    }
    //게시글 상세 이거 확인
//    @GetMapping("/{postId}")
//    public String postDetail(@PathVariable Long postId, Model model) {
//        PostDto postDto = postService.getPostById(postId);
//        model.addAttribute("post", postDto);
//        return "post-detail";
//    }
    @GetMapping("/create")
    public String createPost(Model model) {
        List<Category> categories = postService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postCreateDto", new PostCreateDto());
        return "post-create";
    }

    //게시글 생성 리디렉션
    @PostMapping("/create")
    public String createPost(@ModelAttribute PostCreateDto postCreateDto) {
            PostDto createdPost = postService.createPost(postCreateDto);
            return "redirect:/";
    }

    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId) {
        return "";
    }
    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/post";
    }

    //게시글 수정
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
    //게시글 수정 리디렉션
    @PostMapping("/edit/{postId}")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostUpdateDto postUpdateDto) {
        postService.updatePost(postId, postUpdateDto);
        return "redirect:/post/" + postId;
    }

    //게시글 리스트
    @GetMapping
    public String boardList(Model model) {
        List<PostDto> allPosts = postService.getAllPosts();
        model.addAttribute("posts", allPosts);
        model.addAttribute("categoryName", null);
        return "post-list";
    }
    //카테고리 게시물 리스트
    @GetMapping("/name/{categoryName}")
    public String boardListByCategoryName(@PathVariable String categoryName, Model model) {
        List<PostDto> posts = postService.getPostsByCategoryName(categoryName);
        model.addAttribute("posts", posts);
        model.addAttribute("categoryName", categoryName);
        return "post-list";
    }
}
