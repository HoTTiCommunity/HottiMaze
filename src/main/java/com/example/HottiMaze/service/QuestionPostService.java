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

    public Page<QuestionPostDto> getPostsByMgId(Long mgId, Pageable pageable) {
        return questionPostRepository.findAllByMgId(mgId, pageable)
                .map(this::convertToDto);
    }

    public QuestionPostDto getPost(Long id) {
        QuestionPost post = questionPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        questionPostRepository.save(post);
        return convertToDto(post);
    }

    public QuestionPostDto createPost(QuestionPostDto dto) {
        QuestionPost post = QuestionPost.builder()
                .mgId(dto.getMgId())
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
                .mgId(post.getMgId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}