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
public class MazeUpdateDto {
    private String mazeTitle;
    private MultipartFile mainImage;
    private List<Long> existingQuestionIds;
    private List<String> questionTitles;
    private List<MultipartFile> questionImages;
    private List<String> correctAnswers;
    private List<Integer> pointsList;
    private List<String> hints;
}