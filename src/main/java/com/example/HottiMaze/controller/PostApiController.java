package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.PostCreateDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.dto.PostUpdateDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
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

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateDto createDto) {
            PostDto createdPost = postService.createPost(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
                                              @RequestBody PostUpdateDto updateDto) {
            PostDto updatedPost = postService.updatePost(postId, updateDto);
            return ResponseEntity.ok(updatedPost);
    }
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
            postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        postService.gaechuPost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{id}/dislike")
    public ResponseEntity<Void> dislikePost(@PathVariable Long id) {
        postService.bechuPost(id);
        return ResponseEntity.ok().build();
    }
}