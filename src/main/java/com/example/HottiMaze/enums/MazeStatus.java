package com.example.HottiMaze.enums;

public enum MazeStatus {
    PENDING("PENDING", "승인 대기"),
    APPROVED("APPROVED", "승인됨"),
    REJECTED("REJECTED", "거부됨");

    private final String status;
    private final String description;

    MazeStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}