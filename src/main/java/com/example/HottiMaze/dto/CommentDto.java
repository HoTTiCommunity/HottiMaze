package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String username; // 작성자 ID
    private String userDisplayName; // 표시용 작성자 이름 (예: user***)
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isMyComment; // 현재 로그인한 사용자가 작성한 댓글인지 여부
}