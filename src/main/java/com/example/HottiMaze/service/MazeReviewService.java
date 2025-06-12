// src/main/java/com/example/HottiMaze/service/MazeReviewService.java
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
     * 미로 완주 기록 생성 (내용 없는 완주 기록용)
     */
    @Transactional
    public void createCompletionRecord(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

        // 이미 완주 기록이 있는지 확인
        Optional<MazeReview> existingRecord = mazeReviewRepository.findByMazeIdAndUserId(mazeId, user.getId());

        if (existingRecord.isEmpty()) {
            // 완주 기록용 빈 리뷰 생성
            MazeReview completionRecord = new MazeReview();
            completionRecord.setMaze(maze);
            completionRecord.setUser(user);
            completionRecord.setContent(""); // 빈 내용 (완주 기록용)
            completionRecord.setIsCompleted(true); // 완주 표시

            mazeReviewRepository.save(completionRecord);
        }
    }

    /**
     * 사용자가 미로를 완주했는지 확인
     */
    @Transactional(readOnly = true)
    public boolean hasUserCompleted(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        return mazeReviewRepository.findByMazeIdAndUserId(mazeId, user.getId()).isPresent();
    }

    /**
     * 미로 리뷰 작성 (완주 검증 추가)
     */
    @Transactional
    public MazeReviewDto createReview(String username, MazeReviewCreateDto createDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Maze maze = mazeRepository.findById(createDto.getMazeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + createDto.getMazeId()));

        // 완주 여부 확인
        Optional<MazeReview> existingRecord = mazeReviewRepository.findByMazeIdAndUserId(
                createDto.getMazeId(), user.getId());

        if (existingRecord.isEmpty()) {
            throw new IllegalStateException("미로를 완주한 후에 리뷰를 작성할 수 있습니다.");
        }

        MazeReview record = existingRecord.get();

        // 이미 리뷰 내용이 있다면 (빈 문자열이 아니라면) 중복 작성
        if (record.getContent() != null && !record.getContent().trim().isEmpty()) {
            throw new IllegalStateException("이미 이 미로에 대한 리뷰를 작성하셨습니다.");
        }

        // 리뷰 내용 검증
        if (createDto.getContent() == null || createDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }

        // 기존 완주 기록에 리뷰 내용 추가
        record.setContent(createDto.getContent().trim());
        record.setIsCompleted(true); // 이미 true지만 명시적으로 설정

        MazeReview savedReview = mazeReviewRepository.save(record);
        return convertToDto(savedReview, username);
    }

    /**
     * 특정 미로의 모든 리뷰 조회 (내용이 있는 리뷰만)
     */
    @Transactional(readOnly = true)
    public List<MazeReviewDto> getMazeReviews(Long mazeId, String currentUsername) {
        List<MazeReview> allRecords = mazeReviewRepository.findByMazeIdOrderByCreatedAtDesc(mazeId);

        // 수정: isCompleted가 true인 모든 리뷰를 가져오도록 변경
        // 내용이 비어있어도 완주 기록이므로 표시
        List<MazeReview> actualReviews = allRecords.stream()
                .filter(record -> record.getIsCompleted() != null && record.getIsCompleted())
                .collect(Collectors.toList());

        return actualReviews.stream()
                .map(review -> convertToDto(review, currentUsername))
                .collect(Collectors.toList());
    }

    /**
     * 미로 리뷰 개수 조회 (내용이 있는 리뷰만)
     */
    @Transactional(readOnly = true)
    public long getMazeReviewCount(Long mazeId) {
        List<MazeReview> allRecords = mazeReviewRepository.findByMazeIdOrderByCreatedAtDesc(mazeId);

        // 수정: isCompleted가 true인 리뷰 중 내용이 있는 것만 카운트하도록 변경
        return allRecords.stream()
                .filter(record -> record.getIsCompleted() != null && record.getIsCompleted() &&
                        record.getContent() != null && !record.getContent().trim().isEmpty())
                .count();
    }

    /**
     * 사용자가 특정 미로에 리뷰를 작성했는지 확인 (내용이 있는 리뷰)
     */
    @Transactional(readOnly = true)
    public boolean hasUserReviewed(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Optional<MazeReview> record = mazeReviewRepository.findByMazeIdAndUserId(mazeId, user.getId());

        // 완주 기록은 있지만 리뷰 내용이 없으면 false
        return record.isPresent() &&
                record.get().getContent() != null &&
                !record.get().getContent().trim().isEmpty();
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

    // New method to get the completion count
    @Transactional(readOnly = true)
    public long getMazeCompletionCount(Long mazeId) { //
        return mazeReviewRepository.countByMazeIdAndIsCompleted(mazeId, true); //
    }

    private MazeReviewDto convertToDto(MazeReview review, String currentUsername) {
        MazeReviewDto dto = new MazeReviewDto();
        dto.setId(review.getId());
        dto.setMazeId(review.getMaze().getId());
        dto.setMazeTitle(review.getMaze().getMazeTitle());
        dto.setUsername(review.getUser().getUsername());

        String originalUsername = review.getUser().getUsername();
        if (originalUsername.length() <= 2) {
            // 한 글자 사용자명 처리: 첫 글자 + *** (예: 'a' -> 'a***')
            dto.setUserDisplayName(originalUsername.charAt(0) + "***");
        } else {
            // 두 글자 이상 사용자명 처리: 앞 두 글자 + *** (예: 'user' -> 'us***')
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