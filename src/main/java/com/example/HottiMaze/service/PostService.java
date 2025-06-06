// src/main/java/com/example/HottiMaze/service/PostService.java
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

    /** 전체 게시글 조회 */
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** 카테고리 ID로 게시글 조회 */
    public List<PostDto> getPostsByCategory(Long categoryId) {
        return postRepository.findAllByCategory_Id(categoryId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 이름으로 게시글 조회
     * (DB에 저장된 정확한 카테고리 이름과 일치해야 함)
     */
    @Transactional(readOnly = true)
    public List<PostDto> getPostsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 카테고리입니다: " + categoryName));
        return postRepository.findAllByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /** 전체 카테고리 목록 조회 */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** 카테고리별 게시글 개수 조회 */
    public long getPostCountByCategory(Long categoryId) {
        return postRepository.findAllByCategory_Id(categoryId).size();
    }

    /** 단일 게시글 조회 (DTO) */
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. ID: " + postId));
        return convertToDto(post);
    }

    @Transactional(readOnly = true)
    public Post getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. ID: " + postId));
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setNickname(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setGaechu(post.getGaechu());
        dto.setBechu(post.getBechu());
        return dto;
    }

    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));
        return convertToDto(post);
    }

        dto.setViewCount(post.getViewCount());
        return dto;
    }

    /** 게시글 생성 */
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
        // 필요 시: post.setAuthor(updateDto.getUpdatedBy());

        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    /** 게시글 삭제 */
    @Transactional

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId));
        postRepository.delete(post);
    }

    /** 내부: 엔티티 → DTO 변환 */
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setNickname(post.getAuthor());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setViewCount(post.getViewCount());
        return dto;
    }
}

    public void gaechuPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setGaechu(post.getGaechu() + 1);
        postRepository.save(post);
    }

    public void bechuPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setBechu(post.getBechu() + 1);
        postRepository.save(post);
    }


    @Transactional(readOnly = true)
    public Post getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. ID: " + postId));
    }
}

