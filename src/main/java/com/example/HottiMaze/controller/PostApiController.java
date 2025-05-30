package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = postService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Long categoryId) {
        List<PostDto> posts = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/name/{categoryName}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategoryName(@PathVariable String categoryName) {
        List<PostDto> posts = postService.getPostsByCategoryName(categoryName);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{categoryId}/posts/count")
    public ResponseEntity<Map<String, Object>> getPostCountByCategory(@PathVariable Long categoryId) {
        long count = postService.getPostCountByCategory(categoryId);
        Map<String, Object> response = new HashMap<>();
        response.put("categoryId", categoryId);
        response.put("postCount", count);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{categoryId}/posts")
    public ResponseEntity<> createPost(@PathVariable Long categoryId, @RequestBody PostDto postDto) {
    }
}