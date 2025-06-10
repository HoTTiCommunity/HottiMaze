package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MazeVoteStatsDto {
    private Long mazeId;
    private long totalVotes;
    private long likeCount;
    private long dislikeCount;
    private double likeRatio;
    private double dislikeRatio;
    private Boolean userVote;

    public void calculateRatios() {
        if (totalVotes > 0) {
            this.likeRatio = (double) likeCount / totalVotes;
            this.dislikeRatio = (double) dislikeCount / totalVotes;
        } else {
            this.likeRatio = 0.0;
            this.dislikeRatio = 0.0;
        }
    }
    public int getLikePercentage() {
        return (int) Math.round(likeRatio * 100);
    }

    public int getDislikePercentage() {
        return (int) Math.round(dislikeRatio * 100);
    }
}