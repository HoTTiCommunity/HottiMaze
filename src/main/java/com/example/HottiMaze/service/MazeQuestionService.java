package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeQuestionDto;
import com.example.HottiMaze.entity.MazeQuestion;
import com.example.HottiMaze.repository.MazeQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeQuestionService {

    private final MazeQuestionRepository mazeQuestionRepository;

    public List<MazeQuestionDto> getMazeQuestions(Long mazeId) {
        return mazeQuestionRepository.findByMazeIdOrderByQuestionOrderAsc(mazeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeQuestionDto> getAllMazeQuestions(Long mazeId) {
        return mazeQuestionRepository.findByMazeIdOrderByCreatedAtDesc(mazeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MazeQuestionDto convertToDto(MazeQuestion mazeQuestion) {
        MazeQuestionDto dto = new MazeQuestionDto();
        dto.setId(mazeQuestion.getId());
        dto.setCreatedAt(mazeQuestion.getCreatedAt());
        dto.setMazeId(mazeQuestion.getMaze().getId());
        dto.setUpdatedAt(mazeQuestion.getUpdatedAt());
        dto.setTitle(mazeQuestion.getTitle());
        dto.setContent(mazeQuestion.getContent());
        dto.setAuthor(mazeQuestion.getAuthor());

        // 퀴즈 관련 필드들
        dto.setQuestionImage(mazeQuestion.getQuestionImage());
        dto.setOption1(mazeQuestion.getOption1());
        dto.setOption2(mazeQuestion.getOption2());
        dto.setOption3(mazeQuestion.getOption3());
        dto.setOption4(mazeQuestion.getOption4());
        dto.setCorrectAnswer(mazeQuestion.getCorrectAnswer());
        dto.setQuestionOrder(mazeQuestion.getQuestionOrder());

        // 디버깅용 로그
        System.out.println("DTO 변환 - ID: " + dto.getId() + ", 제목: " + dto.getTitle() + ", 내용: " + dto.getContent());
        System.out.println("선택지들: " + dto.getOption1() + ", " + dto.getOption2() + ", " + dto.getOption3() + ", " + dto.getOption4());

        return dto;
    }
}