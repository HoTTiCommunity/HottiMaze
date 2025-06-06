package com.example.HottiMaze.service;


import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.repository.MazeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.HottiMaze.dto.MazeCreateDto;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.entity.MazeQuestion;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.MazeQuestionRepository;
import com.example.HottiMaze.repository.MazeRepository;
import com.example.HottiMaze.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MazeService {

    private final MazeRepository mazeRepository;
    private final MazeQuestionRepository mazeQuestionRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

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

    public MazeDto getMaze(Long id) {
        Maze maze = mazeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + id));
        return convertToDto(maze);
    }

    // 새로운 미궁 생성 메서드 - mazeDir null 오류 해결
    @Transactional
    public MazeDto createMaze(MazeCreateDto createDto) {
        try {
            // 사용자 조회 (임시로 첫 번째 사용자 사용, 실제로는 로그인된 사용자)
            User creator = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 임시 ID를 생성하여 파일 저장 경로 만들기
            Long tempMazeId = System.currentTimeMillis(); // 임시 ID로 현재 시간 사용

            // 메인 이미지 먼저 저장
            String mainImagePath = null;
            if (createDto.getMainImage() != null && !createDto.getMainImage().isEmpty()) {
                mainImagePath = fileUploadService.saveFile(
                        createDto.getMainImage(),
                        tempMazeId,
                        "main"
                );
            } else {
                throw new IllegalArgumentException("메인 이미지는 필수입니다.");
            }

            // 미로 엔티티 생성 및 저장 (이제 mazeDir이 null이 아님)
            Maze maze = new Maze();
            maze.setMazeTitle(createDto.getMazeTitle());
            maze.setMazeDir(mainImagePath); // null이 아닌 값으로 설정
            maze.setUser(creator);
            maze.setCreatedAt(LocalDateTime.now());
            maze.setUpdatedAt(LocalDateTime.now());
            maze.setViewCount(0);

            // 미로 저장
            Maze savedMaze = mazeRepository.save(maze);

            // 실제 ID와 임시 ID가 다르면 파일명 변경
            if (!savedMaze.getId().equals(tempMazeId)) {
                String newImagePath = fileUploadService.renameFile(mainImagePath, tempMazeId, savedMaze.getId());
                savedMaze.setMazeDir(newImagePath);
                savedMaze = mazeRepository.save(savedMaze);
            }

            // 문제들 생성 및 저장
            if (createDto.getQuestionImages() != null && !createDto.getQuestionImages().isEmpty()) {
                createMazeQuestions(savedMaze, createDto);
            }

            return convertToDto(savedMaze);

        } catch (Exception e) {
            throw new RuntimeException("미로 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Transactional
    protected void createMazeQuestions(Maze maze, MazeCreateDto createDto) {
        List<MultipartFile> questionImages = createDto.getQuestionImages();

        for (int i = 0; i < questionImages.size(); i++) {
            MultipartFile questionImage = questionImages.get(i);

            if (questionImage != null && !questionImage.isEmpty()) {
                try {
                    // 문제 이미지 저장 (maze{id} 폴더 사용)
                    String questionImagePath = fileUploadService.saveFile(
                            questionImage,
                            maze.getId(),
                            "question" + (i + 1)
                    );

                    // 문제 엔티티 생성
                    MazeQuestion question = new MazeQuestion();
                    question.setMaze(maze);
                    question.setQuestionImage(questionImagePath);
                    question.setQuestionOrder(i + 1);
                    question.setCreatedAt(LocalDateTime.now());
                    question.setUpdatedAt(LocalDateTime.now());

                    // 선택적 필드들 설정
                    setQuestionField(question::setTitle, createDto.getQuestionTitles(), i, "문제 " + (i + 1));
                    setQuestionField(question::setCorrectAnswer, createDto.getCorrectAnswers(), i, "test");
                    setQuestionNumberField(question::setPoints, createDto.getPointsList(), i, 10);

                    mazeQuestionRepository.save(question);

                } catch (Exception e) {
                    throw new RuntimeException("문제 " + (i + 1) + " 생성 중 오류: " + e.getMessage(), e);
                }
            }
        }
    }

    private void setQuestionField(java.util.function.Consumer<String> setter, List<String> list, int index, String defaultValue) {
        if (list != null && index < list.size() && list.get(index) != null && !list.get(index).trim().isEmpty()) {
            setter.accept(list.get(index).trim());
        } else if (defaultValue != null) {
            setter.accept(defaultValue);
        }
    }

    private void setQuestionNumberField(java.util.function.Consumer<Integer> setter, List<Integer> list, int index, int defaultValue) {
        if (list != null && index < list.size() && list.get(index) != null) {
            setter.accept(list.get(index));
        } else {
            setter.accept(defaultValue);
        }
    }

    @Transactional
    public void deleteMaze(Long mazeId) {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

        // 미로 폴더 전체 삭제 (maze{id} 폴더와 그 안의 모든 파일들)
        fileUploadService.deleteMazeFolder(mazeId);

        // 데이터베이스에서 삭제 (연관 문제들도 함께 삭제됨)
        mazeRepository.delete(maze);
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