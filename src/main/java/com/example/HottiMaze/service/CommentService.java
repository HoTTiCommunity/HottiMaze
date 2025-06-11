package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.CommentCreateDto;
import com.example.HottiMaze.dto.CommentDto;
import com.example.HottiMaze.dto.CommentUpdateDto;
import com.example.HottiMaze.entity.Comment;
import com.example.HottiMaze.entity.Post;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.CommentRepository;
import com.example.HottiMaze.repository.PostRepository;
import com.example.HottiMaze.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 생성
     * @param username 현재 로그인한 사용자 ID
     * @param createDto 댓글 생성 정보 (postId, content)
     * @return 생성된 댓글 DTO
     */
    @Transactional
    public CommentDto createComment(String username, CommentCreateDto createDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Post post = postRepository.findById(createDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다: " + createDto.getPostId()));

        if (createDto.getContent() == null || createDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(createDto.getContent().trim());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment, username);
    }

    /**
     * 특정 게시글의 모든 댓글 조회
     * @param postId 게시글 ID
     * @param currentUsername 현재 로그인한 사용자명 (내 댓글 여부 확인용)
     * @return 댓글 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(Long postId, String currentUsername) {
        // 게시글 존재 여부 확인 (선택 사항, 그러나 안전성을 위해)
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다: " + postId);
        }

        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId).stream()
                .map(comment -> convertToDto(comment, currentUsername))
                .collect(Collectors.toList());
    }

    /**
     * 특정 댓글 조회
     * @param commentId 댓글 ID
     * @param postId 게시글 ID (URL 경로와 일치하는지 검증)
     * @param currentUsername 현재 로그인한 사용자명 (내 댓글 여부 확인용)
     * @return 댓글 DTO
     */
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId, Long postId, String currentUsername) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다: " + commentId));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("이 댓글은 해당 게시글에 속하지 않습니다.");
        }

        return convertToDto(comment, currentUsername);
    }

    /**
     * 댓글 수정
     * @param commentId 댓글 ID
     * @param username 현재 로그인한 사용자 ID
     * @param updateDto 댓글 수정 정보 (content)
     * @return 수정된 댓글 DTO
     */
    @Transactional
    public CommentDto updateComment(Long commentId, String username, CommentUpdateDto updateDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다: " + commentId));

        // 작성자만 수정 가능
        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        if (updateDto.getContent() == null || updateDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        comment.setContent(updateDto.getContent().trim());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);
        return convertToDto(updatedComment, username);
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 ID
     * @param username 현재 로그인한 사용자 ID
     */
    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다: " + commentId));

        // 작성자만 삭제 가능 (또는 관리자)
        // 관리자 권한 확인 로직은 SecurityUtils 등을 활용하여 추가 가능
        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    /**
     * 특정 게시글의 댓글 개수 조회
     * @param postId 게시글 ID
     * @return 댓글 개수
     */
    @Transactional(readOnly = true)
    public long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    /**
     * 엔티티를 DTO로 변환하는 헬퍼 메서드
     * @param comment 변환할 Comment 엔티티
     * @param currentUsername 현재 로그인한 사용자명
     * @return CommentDto
     */
    private CommentDto convertToDto(Comment comment, String currentUsername) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setUsername(comment.getUser().getUsername());

        // 사용자 표시명 처리 (예: user001 -> u***)
        String originalUsername = comment.getUser().getUsername();
        if (originalUsername.length() <= 2) {
            dto.setUserDisplayName(originalUsername.charAt(0) + "***");
        } else {
            dto.setUserDisplayName(originalUsername.substring(0, 2) + "***");
        }

        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setMyComment(currentUsername != null && currentUsername.equals(comment.getUser().getUsername()));
        return dto;
    }
}