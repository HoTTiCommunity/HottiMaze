package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
}
