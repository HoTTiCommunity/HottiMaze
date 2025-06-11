package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.QuestionPostDto;
import com.example.HottiMaze.entity.QuestionPost;
import com.example.HottiMaze.repository.QuestionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionPostService {

    private final QuestionPostRepository questionPostRepository;

    // 특정 미로의 특정 문제에 대한 질문들 조회
    public Page<QuestionPostDto> getPostsByMazeIdAndQuestionOrder(
            Long mazeId, Integer questionOrder, Pageable pageable) {
        return questionPostRepository.findByMazeIdAndQuestionOrderOrderByCreatedAtDesc(
                        mazeId, questionOrder, pageable)
                .map(this::convertToDto);
    }

    // 특정 미로의 모든 질문들 조회 (선택사항)
    public Page<QuestionPostDto> getPostsByMazeId(Long mazeId, Pageable pageable) {
        return questionPostRepository.findByMazeIdOrderByQuestionOrderAscCreatedAtDesc(
                        mazeId, pageable)
                .map(this::convertToDto);
    }

    public QuestionPostDto getPost(Long id) {
        QuestionPost post = questionPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        return convertToDto(post);
    }

    public QuestionPostDto createPost(QuestionPostDto dto) {
        QuestionPost post = QuestionPost.builder()
                .mazeId(dto.getMazeId())
                .questionOrder(dto.getQuestionOrder())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        QuestionPost saved = questionPostRepository.save(post);
        return convertToDto(saved);
    }

    public QuestionPostDto updatePost(Long id, QuestionPostDto dto) {
        QuestionPost post = questionPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        QuestionPost updated = questionPostRepository.save(post);
        return convertToDto(updated);
    }

    public void deletePost(Long id) {
        questionPostRepository.deleteById(id);
    }

    private QuestionPostDto convertToDto(QuestionPost post) {
        return QuestionPostDto.builder()
                .id(post.getId())
                .mazeId(post.getMazeId())
                .questionOrder(post.getQuestionOrder())
                .title(post.getTitle())
                .author(post.getAuthor())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}