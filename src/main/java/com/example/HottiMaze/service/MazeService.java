package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.repository.MazeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeService {

    private final MazeRepository mazeRepository;

    // 최신 미로 목록 조회 (최대 5개)
    public List<MazeDto> getLatestMazes() {
        return mazeRepository.findLatestMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 인기 미로 목록 조회 (최대 5개)
    public List<MazeDto> getPopularMazes() {
        return mazeRepository.findPopularMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getAllMazes() {
        return mazeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public MazeDto getMaze(Long id) {
        Maze maze = mazeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No id : " + id));
        return convertToDto(maze);
    }

    private MazeDto convertToDto(Maze maze) {
        MazeDto dto = new MazeDto();
        dto.setId(maze.getId());
        dto.setMazeTitle(maze.getMazeTitle());
        dto.setMazeDir(maze.getMazeDir());
        dto.setCreatorName(maze.getUser() != null ? maze.getUser().getUsername() : "익명");
        dto.setCreatedAt(maze.getCreatedAt());
        dto.setUpdatedAt(maze.getUpdatedAt());
        dto.setViewCount(maze.getViewCount());
        return dto;
    }
}