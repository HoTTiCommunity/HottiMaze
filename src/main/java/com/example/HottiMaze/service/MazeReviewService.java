package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeReviewCreateDto;
import com.example.HottiMaze.dto.MazeReviewDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.entity.MazeReview;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.MazeRepository;
import com.example.HottiMaze.repository.MazeReviewRepository;
import com.example.HottiMaze.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeReviewService {

    private final MazeReviewRepository mazeReviewRepository;
    private final MazeRepository mazeRepository;
    private final UserRepository userRepository;

    /**
     * 미로 리뷰 작성
     */
    @Transactional
    public MazeReviewDto createReview(String username, MazeReviewCreateDto createDto) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        // 미로 조회
        Maze maze = mazeRepository.findById(createDto.getMazeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + createDto.getMazeId()));

        // 이미 리뷰를 작성했는지 확인
        Optional<MazeReview> existingReview = mazeReviewRepository.findByMazeIdAndUserId(
                createDto.getMazeId(), user.getId());

        if (existingReview.isPresent()) {
            throw new IllegalStateException("이미 이 미로에 대한 리뷰를 작성하셨습니다.");
        }

        // 리뷰 내용 검증
        if (createDto.getContent() == null || createDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }

        // 리뷰 생성
        MazeReview review = new MazeReview();
        review.setMaze(maze);
        review.setUser(user);
        review.setContent(createDto.getContent().trim());
        review.setIsCompleted(true);

        MazeReview savedReview = mazeReviewRepository.save(review);
        return convertToDto(savedReview, username);
    }

    /**
     * 특정 미로의 모든 리뷰 조회
     */
    @Transactional(readOnly = true)
    public List<MazeReviewDto> getMazeReviews(Long mazeId, String currentUsername) {
        List<MazeReview> reviews = mazeReviewRepository.findByMazeIdOrderByCreatedAtDesc(mazeId);
        return reviews.stream()
                .map(review -> convertToDto(review, currentUsername))
                .collect(Collectors.toList());
    }

    /**
     * 미로 리뷰 개수 조회
     */
    @Transactional(readOnly = true)
    public long getMazeReviewCount(Long mazeId) {
        return mazeReviewRepository.countByMazeId(mazeId);
    }

    /**
     * 사용자가 특정 미로에 리뷰를 작성했는지 확인
     */
    @Transactional(readOnly = true)
    public boolean hasUserReviewed(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        return mazeReviewRepository.findByMazeIdAndUserId(mazeId, user.getId()).isPresent();
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public MazeReviewDto updateReview(Long reviewId, String username, MazeReviewCreateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        MazeReview review = mazeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다: " + reviewId));

        // 작성자 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 리뷰 내용 검증
        if (updateDto.getContent() == null || updateDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }

        // 수정
        review.setContent(updateDto.getContent().trim());

        MazeReview updatedReview = mazeReviewRepository.save(review);
        return convertToDto(updatedReview, username);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        MazeReview review = mazeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다: " + reviewId));

        // 작성자 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        mazeReviewRepository.delete(review);
    }

    /**
     * 미로 삭제 시 관련 리뷰들도 삭제
     */
    @Transactional
    public void deleteReviewsByMazeId(Long mazeId) {
        mazeReviewRepository.deleteByMazeId(mazeId);
    }

    /**
     * 엔티티를 DTO로 변환
     */
    private MazeReviewDto convertToDto(MazeReview review, String currentUsername) {
        MazeReviewDto dto = new MazeReviewDto();
        dto.setId(review.getId());
        dto.setMazeId(review.getMaze().getId());
        dto.setMazeTitle(review.getMaze().getMazeTitle());
        dto.setUsername(review.getUser().getUsername());

        // 사용자명 익명 처리 (처음 2글자 + ***)
        String originalUsername = review.getUser().getUsername();
        if (originalUsername.length() <= 2) {
            dto.setUserDisplayName(originalUsername.charAt(0) + "***");
        } else {
            dto.setUserDisplayName(originalUsername.substring(0, 2) + "***");
        }

        dto.setContent(review.getContent());
        dto.setIsCompleted(review.getIsCompleted());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setMyReview(currentUsername != null && currentUsername.equals(review.getUser().getUsername()));

        return dto;
    }
}