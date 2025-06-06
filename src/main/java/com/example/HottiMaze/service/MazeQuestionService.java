package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeDto;
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

    public List<MazeQuestionDto> getMazeQuestions(Long MazeId) {
        return mazeQuestionRepository.findByMazeIdOrderByCreatedAtDesc(MazeId)
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
        return dto;
    }
}
