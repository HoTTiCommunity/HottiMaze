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
    private Long totalVotes;
    private Long likeCount;
    private Long dislikeCount;
    private Double likeRatio;
    private Double dislikeRatio;
    private Boolean userVote;

    public void calculateVoteRatios() {
        if (totalVotes != null && totalVotes > 0) {
            this.likeRatio = likeCount != null ? (double) likeCount / totalVotes : 0.0;
            this.dislikeRatio = dislikeCount != null ? (double) dislikeCount / totalVotes : 0.0;
        } else {
            this.likeRatio = 0.0;
            this.dislikeRatio = 0.0;
        }
    }

    public int getLikePercentage() {
        return likeRatio != null ? (int) Math.round(likeRatio * 100) : 0;
    }

    public int getDislikePercentage() {
        return dislikeRatio != null ? (int) Math.round(dislikeRatio * 100) : 0;
    }

    public boolean hasVoteStats() {
        return totalVotes != null && likeCount != null && dislikeCount != null;
    }

    public void initializeEmptyVoteStats() {
        this.totalVotes = 0L;
        this.likeCount = 0L;
        this.dislikeCount = 0L;
        this.likeRatio = 0.0;
        this.dislikeRatio = 0.0;
        this.userVote = null;
    }

    public void setVoteStats(Long likeCount, Long dislikeCount, Boolean userVote) {
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.dislikeCount = dislikeCount != null ? dislikeCount : 0L;
        this.totalVotes = this.likeCount + this.dislikeCount;
        this.userVote = userVote;
        calculateVoteRatios();
    }
}