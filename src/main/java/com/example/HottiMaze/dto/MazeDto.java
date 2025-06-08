package com.example.HottiMaze.dto;

import com.example.HottiMaze.enums.MazeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MazeDto {
    private Long id;
    private String mazeTitle;
    private String mazeDir;
    private String creatorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
    private MazeStatus status;
    private LocalDateTime approvedAt;
    private String approvedByUsername;
    private String rejectionReason;

    // 투표 관련 필드 추가 (선택적으로 사용)
    private Long totalVotes;        // 전체 투표 수
    private Long likeCount;         // 좋아요 수
    private Long dislikeCount;      // 싫어요 수
    private Double likeRatio;       // 좋아요 비율 (0.0 ~ 1.0)
    private Double dislikeRatio;    // 싫어요 비율 (0.0 ~ 1.0)
    private Boolean userVote;       // 현재 사용자의 투표 상태 (null: 투표안함, true: 좋아요, false: 싫어요)

    // 투표 통계 계산 메서드
    public void calculateVoteRatios() {
        if (totalVotes != null && totalVotes > 0) {
            this.likeRatio = likeCount != null ? (double) likeCount / totalVotes : 0.0;
            this.dislikeRatio = dislikeCount != null ? (double) dislikeCount / totalVotes : 0.0;
        } else {
            this.likeRatio = 0.0;
            this.dislikeRatio = 0.0;
        }
    }

    // 좋아요 퍼센티지 반환
    public int getLikePercentage() {
        return likeRatio != null ? (int) Math.round(likeRatio * 100) : 0;
    }

    // 싫어요 퍼센티지 반환
    public int getDislikePercentage() {
        return dislikeRatio != null ? (int) Math.round(dislikeRatio * 100) : 0;
    }

    // 투표 통계가 설정되어 있는지 확인
    public boolean hasVoteStats() {
        return totalVotes != null && likeCount != null && dislikeCount != null;
    }

    // 투표 통계 초기화 (투표 데이터가 없을 때)
    public void initializeEmptyVoteStats() {
        this.totalVotes = 0L;
        this.likeCount = 0L;
        this.dislikeCount = 0L;
        this.likeRatio = 0.0;
        this.dislikeRatio = 0.0;
        this.userVote = null;
    }

    // 투표 통계 설정
    public void setVoteStats(Long likeCount, Long dislikeCount, Boolean userVote) {
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.dislikeCount = dislikeCount != null ? dislikeCount : 0L;
        this.totalVotes = this.likeCount + this.dislikeCount;
        this.userVote = userVote;
        calculateVoteRatios();
    }
}