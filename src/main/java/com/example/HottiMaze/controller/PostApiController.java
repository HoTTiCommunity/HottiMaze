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
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 추가
import org.springframework.security.core.userdetails.UserDetails; // 추가
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
    public ResponseEntity<Map<String, Object>> likePost( // ResponseEntity 반환 타입 변경
                                                         @PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails principal) { // UserDetails 파라미터 추가
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            postService.gaechuPost(id, principal.getUsername());
            response.put("success", true);
            response.put("message", "좋아요 처리되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalStateException e) { // 중복 투표 시 처리 (선택)
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/posts/{id}/dislike")
    public ResponseEntity<Map<String, Object>> dislikePost( // ResponseEntity 반환 타입 변경
                                                            @PathVariable Long id,
                                                            @AuthenticationPrincipal UserDetails principal) { // UserDetails 파라미터 추가
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            postService.bechuPost(id, principal.getUsername());
            response.put("success", true);
            response.put("message", "싫어요 처리되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalStateException e) { // 중복 투표 시 처리 (선택)
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}