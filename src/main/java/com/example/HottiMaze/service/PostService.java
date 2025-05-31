package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.PostCreateDto;
import com.example.HottiMaze.dto.PostDto;
import com.example.HottiMaze.dto.PostUpdateDto;
import com.example.HottiMaze.entity.Category;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.repository.CategoryRepository;
import com.example.HottiMaze.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getPostsByCategory(Long categoryId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getCategory() != null && post.getCategory().getId().equals(categoryId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getPostsByCategoryName(String categoryName) {
        return postRepository.findAll().stream()
                .filter(post -> post.getCategory() != null && post.getCategory().getName().equals(categoryName))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public long getPostCountByCategory(Long categoryId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getCategory() != null && post.getCategory().getId().equals(categoryId))
                .count();
    }

    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setNickname(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    @Transactional
    public PostDto createPost(PostCreateDto createDto) {
        Category category = categoryRepository.findById(createDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + createDto.getCategoryId()));
        Post post = new Post();
        post.setTitle(createDto.getTitle());
        post.setContent(createDto.getContent());
        post.setAuthor(createDto.getAuthor());
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setViewCount(0);
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }
    @Transactional
    public PostDto updatePost(Long postId, PostUpdateDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));
        Category category = categoryRepository.findById(updateDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + updateDto.getCategoryId()));
        post.setTitle(updateDto.getTitle());
        post.setContent(updateDto.getContent());
        post.setCategory(category);
        post.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));
        postRepository.delete(post);
    }
}