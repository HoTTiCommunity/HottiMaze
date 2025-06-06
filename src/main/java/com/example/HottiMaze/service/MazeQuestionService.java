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
        dto.setMazeId(mazeQuestion.getMaze().getId());
        dto.setQuestionImage(mazeQuestion.getQuestionImage());
        dto.setCorrectAnswer(mazeQuestion.getCorrectAnswer());
        dto.setQuestionOrder(mazeQuestion.getQuestionOrder());
        dto.setCreatedAt(mazeQuestion.getCreatedAt());
        dto.setUpdatedAt(mazeQuestion.getUpdatedAt());

        return dto;
    }
}