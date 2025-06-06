package com.example.HottiMaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MazeCreateDto {
    private String mazeTitle;           // 미로 제목
    private String creatorName;         // 제작자 이름 (User 참조용)
    private MultipartFile mainImage;    // 메인 이미지 (mazeDir에 저장)

    private List<String> questionTitles;      // 문제 제목들 (title)
    private List<MultipartFile> questionImages; // 문제 이미지들 (questionImage)
    private List<String> correctAnswers;      // 정답들 (correctAnswer)
    private List<Integer> pointsList;        // 점수 목록 (points - 기본값 10)
}