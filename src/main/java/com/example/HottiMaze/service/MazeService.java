package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeApprovalDto;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.enums.MazeStatus;
import com.example.HottiMaze.repository.MazeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.HottiMaze.dto.MazeCreateDto;
import com.example.HottiMaze.entity.MazeQuestion;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.MazeQuestionRepository;
import com.example.HottiMaze.repository.UserRepository;
import com.example.HottiMaze.repository.MazeVoteRepository;
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
    private final MazeVoteRepository mazeVoteRepository;

    /**
     * 최근에 등록된 Maze 5개를 가져오는 메소드
     * @return MazeDto List
     */
    public List<MazeDto> getLatestMazes() {
        return mazeRepository.findLatestMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getPopularMazes() {
        return mazeRepository.findPopularMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getAllApprovedMazes() {
        return mazeRepository.findByStatusOrderByCreatedAtDesc(MazeStatus.APPROVED)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getPendingMazes() {
        return mazeRepository.findByStatusOrderByCreatedAtAsc(MazeStatus.PENDING)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getAllMazesForAdmin() {
        return mazeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MazeDto> getUserMazes(Long userId) {
        return mazeRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MazeDto getMaze(Long id) {
        Maze maze = mazeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + id));
        return convertToDto(maze);
    }

    @Transactional
    public MazeDto createMaze(MazeCreateDto createDto) {
        try {
            User creator = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            Long tempMazeId = System.currentTimeMillis();

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

            Maze maze = new Maze();
            maze.setMazeTitle(createDto.getMazeTitle());
            maze.setMazeDir(mainImagePath);
            maze.setUser(creator);
            maze.setCreatedAt(LocalDateTime.now());
            maze.setUpdatedAt(LocalDateTime.now());
            maze.setViewCount(0);
            maze.setStatus(MazeStatus.PENDING); // 승인 대기 상태로 설정

            Maze savedMaze = mazeRepository.save(maze);

            if (!savedMaze.getId().equals(tempMazeId)) {
                String newImagePath = fileUploadService.renameFile(mainImagePath, tempMazeId, savedMaze.getId());
                savedMaze.setMazeDir(newImagePath);
                savedMaze = mazeRepository.save(savedMaze);
            }

            if (createDto.getQuestionImages() != null && !createDto.getQuestionImages().isEmpty()) {
                createMazeQuestions(savedMaze, createDto);
            }

            return convertToDto(savedMaze);

        } catch (Exception e) {
            throw new RuntimeException("미로 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 미로 조회 및 조회수 증가
     * @param id 미로 ID
     * @return 미로 DTO
     */
    @Transactional
    public MazeDto getMazeAndIncreaseViewCount(Long id) {
        Maze maze = mazeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + id));
        maze.setViewCount(maze.getViewCount() + 1);
        mazeRepository.save(maze);

        return convertToDto(maze);
    }

    @Transactional
    public void approveMaze(Long mazeId, String adminUsername) {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + adminUsername));

        maze.setStatus(MazeStatus.APPROVED);
        maze.setApprovedAt(LocalDateTime.now());
        maze.setApprovedBy(admin);
        maze.setRejectionReason(null);

        mazeRepository.save(maze);
    }

    @Transactional
    public void rejectMaze(Long mazeId, String rejectionReason, String adminUsername, boolean autoDelete) {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

        if (autoDelete) {
            System.out.println("미로 ID " + mazeId + " 거부 및 자동 삭제 시작: " + rejectionReason);

            try {
                deleteAllMazeFiles(mazeId);

                mazeQuestionRepository.deleteAll(
                        mazeQuestionRepository.findByMazeIdOrderByQuestionOrderAsc(mazeId)
                );

                // 투표 데이터 삭제 추가
                mazeVoteRepository.deleteByMazeId(mazeId);
                System.out.println("미로 ID " + mazeId + "의 투표 데이터가 삭제되었습니다.");

                // 미로 엔티티 삭제
                mazeRepository.delete(maze);

                System.out.println("미로 ID " + mazeId + " 거부 후 자동 삭제 완료");

            } catch (Exception e) {
                System.err.println("미로 ID " + mazeId + " 자동 삭제 중 오류 발생: " + e.getMessage());
                updateMazeToRejected(maze, rejectionReason, adminUsername);
                throw new RuntimeException("미로 파일 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
            }
        } else {
            updateMazeToRejected(maze, rejectionReason, adminUsername);
            System.out.println("미로 ID " + mazeId + " 거부됨 (파일 유지): " + rejectionReason);
        }
    }

    private void updateMazeToRejected(Maze maze, String rejectionReason, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다: " + adminUsername));

        maze.setStatus(MazeStatus.REJECTED);
        maze.setRejectionReason(rejectionReason);
        maze.setApprovedBy(admin);
        maze.setApprovedAt(LocalDateTime.now()); // 처리 일시

        mazeRepository.save(maze);
    }

    // 미로 관련 모든 파일 삭제하는 헬퍼 메서드
    private void deleteAllMazeFiles(Long mazeId) {
        try {
            fileUploadService.deleteMazeFolder(mazeId);
            System.out.println("미로 ID " + mazeId + "의 모든 파일이 삭제되었습니다.");
        } catch (Exception e) {
            System.err.println("미로 ID " + mazeId + " 파일 삭제 실패: " + e.getMessage());
        }
    }

    @Transactional
    public void rejectMaze(Long mazeId, String rejectionReason, String adminUsername) {
        rejectMaze(mazeId, rejectionReason, adminUsername, false);
    }

    @Transactional
    public void processMazeApproval(MazeApprovalDto approvalDto, String adminUsername) {
        if (approvalDto.getStatus() == MazeStatus.APPROVED) {
            approveMaze(approvalDto.getMazeId(), adminUsername);
        } else if (approvalDto.getStatus() == MazeStatus.REJECTED) {
            rejectMaze(approvalDto.getMazeId(), approvalDto.getRejectionReason(), adminUsername, false);
        }
    }

    public long getMazeCountByStatus(MazeStatus status) {
        return mazeRepository.countByStatus(status);
    }

    @Transactional
    protected void createMazeQuestions(Maze maze, MazeCreateDto createDto) {
        List<MultipartFile> questionImages = createDto.getQuestionImages();

        for (int i = 0; i < questionImages.size(); i++) {
            MultipartFile questionImage = questionImages.get(i);

            if (questionImage != null && !questionImage.isEmpty()) {
                try {
                    String questionImagePath = fileUploadService.saveFile(
                            questionImage,
                            maze.getId(),
                            "question" + (i + 1)
                    );

                    MazeQuestion question = new MazeQuestion();
                    question.setMaze(maze);
                    question.setQuestionImage(questionImagePath);
                    question.setQuestionOrder(i + 1);
                    question.setCreatedAt(LocalDateTime.now());
                    question.setUpdatedAt(LocalDateTime.now());

                    setQuestionField(question::setTitle, createDto.getQuestionTitles(), i, "문제 " + (i + 1));
                    setQuestionField(question::setCorrectAnswer, createDto.getCorrectAnswers(), i, "test");
                    setQuestionField(question::setHint, createDto.getHints(), i, null);
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

        try {
            // 파일 삭제
            deleteAllMazeFiles(mazeId);

            // 관련 질문 삭제
            List<MazeQuestion> questions = mazeQuestionRepository.findByMazeIdOrderByQuestionOrderAsc(mazeId);
            if (!questions.isEmpty()) {
                mazeQuestionRepository.deleteAll(questions);
                System.out.println("미로 ID " + mazeId + "의 " + questions.size() + "개 질문이 삭제되었습니다.");
            }

            // 투표 데이터 삭제
            mazeVoteRepository.deleteByMazeId(mazeId);
            System.out.println("미로 ID " + mazeId + "의 투표 데이터가 삭제되었습니다.");

            // 미로 엔티티 삭제
            mazeRepository.delete(maze);
            System.out.println("미로 ID " + mazeId + " 완전 삭제 완료");

        } catch (Exception e) {
            System.err.println("미로 ID " + mazeId + " 삭제 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("미로 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
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
        dto.setStatus(maze.getStatus());
        dto.setApprovedAt(maze.getApprovedAt());
        dto.setApprovedByUsername(maze.getApprovedBy() != null ? maze.getApprovedBy().getUsername() : null);
        dto.setRejectionReason(maze.getRejectionReason());

        return dto;
    }
}