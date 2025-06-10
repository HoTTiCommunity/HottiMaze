package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeApprovalDto;
import com.example.HottiMaze.dto.MazeDto;
import com.example.HottiMaze.dto.MazeUpdateDto;
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
import java.util.ArrayList;
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
    private final MazeVoteService mazeVoteService; // MazeVoteService 주입

    /**
     * 최근에 등록된 Maze 5개를 가져오는 메소드
     * @return MazeDto List
     */
    public List<MazeDto> getLatestMazes() {
        return mazeRepository.findLatestMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDtoWithVoteStats) // 투표 통계 포함하여 변환
                .collect(Collectors.toList());
    }

    public List<MazeDto> getPopularMazes() {
        return mazeRepository.findPopularMazes(PageRequest.of(0, 5))
                .stream()
                .map(this::convertToDtoWithVoteStats) // 투표 통계 포함하여 변환
                .collect(Collectors.toList());
    }

    public List<MazeDto> getAllApprovedMazes() {
        return mazeRepository.findByStatusOrderByCreatedAtDesc(MazeStatus.APPROVED)
                .stream()
                .map(this::convertToDtoWithVoteStats) // 투표 통계 포함하여 변환
                .collect(Collectors.toList());
    }

    public List<MazeDto> getPendingMazes() {
        return mazeRepository.findByStatusOrderByCreatedAtAsc(MazeStatus.PENDING)
                .stream()
                .map(this::convertToDto) // 승인 대기 중인 미로는 투표 통계 필요 없음
                .collect(Collectors.toList());
    }

    public List<MazeDto> getAllMazesForAdmin() {
        return mazeRepository.findAll().stream()
                .map(this::convertToDto) // 관리자 페이지에서는 투표 통계 필요 없음
                .collect(Collectors.toList());
    }
    /**
     * userId로 User의 Maze를 가져오는 메소드 입니다
     * @param userId
     * @return MazeDto의 리스트
     */
    public List<MazeDto> getUserMazes(Long userId) {
        return mazeRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDto) // 사용자 프로필에서는 투표 통계 필요 없음
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자의 미로 목록 조회
     * @param username 사용자명
     * @return 미로 목록
     */
    @Transactional(readOnly = true)
    public List<MazeDto> getUserMazesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        return getUserMazes(user.getId());
    }

    public MazeDto getMaze(Long id) {
        Maze maze = mazeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + id));
        return convertToDto(maze);
    }

    @Transactional
    public MazeDto createMaze(MazeCreateDto createDto, String username) {
        try {
            User creator = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

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

    /**
     * 미로 제목 수정
     * @param mazeId 미로 ID
     * @param newTitle 새로운 제목
     */
    @Transactional
    public void updateMazeTitle(Long mazeId, String newTitle) {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("미로 제목을 입력해주세요.");
        }

        maze.setMazeTitle(newTitle.trim());
        maze.setUpdatedAt(LocalDateTime.now());

        mazeRepository.save(maze);

        System.out.println("미로 ID " + mazeId + "의 제목이 '" + newTitle + "'로 수정되었습니다.");
    }

    /**
     * 미로 전체 업데이트 (제목, 이미지, 문제들)
     * @param mazeId 미로 ID
     * @param updateDto 업데이트할 데이터
     * @param username 사용자명
     */
    @Transactional
    public void updateMaze(Long mazeId, MazeUpdateDto updateDto, String username) {
        try {
            validateUpdateData(updateDto);

            Maze maze = mazeRepository.findById(mazeId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId));

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            if (!maze.getUser().getUsername().equals(username)) {
                throw new IllegalArgumentException("본인이 작성한 미로만 수정할 수 있습니다.");
            }
            maze.setMazeTitle(updateDto.getMazeTitle().trim());
            maze.setUpdatedAt(LocalDateTime.now());

            maze.setStatus(MazeStatus.PENDING);
            maze.setApprovedAt(null);
            maze.setApprovedBy(null);
            maze.setRejectionReason(null);

            // 2. 메인 이미지 업데이트 (새 이미지가 있는 경우)
            if (updateDto.getMainImage() != null && !updateDto.getMainImage().isEmpty()) {
                String newMainImagePath = fileUploadService.saveFile(
                        updateDto.getMainImage(),
                        mazeId,
                        "main"
                );

                // 기존 메인 이미지 삭제 (새 이미지로 교체)
                if (maze.getMazeDir() != null) {
                    fileUploadService.deleteFile(maze.getMazeDir());
                }
                maze.setMazeDir(newMainImagePath);
            }

            Maze savedMaze = mazeRepository.save(maze);
            updateMazeQuestions(savedMaze, updateDto);
            System.out.println("미로 ID " + mazeId + " 전체 업데이트 완료");
        } catch (Exception e) {
            throw new RuntimeException("미로 업데이트 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    /**
     * 미로 문제들 업데이트
     */
    @Transactional
    protected void updateMazeQuestions(Maze maze, MazeUpdateDto updateDto) {
        List<MazeQuestion> existingQuestions = mazeQuestionRepository.findByMazeIdOrderByQuestionOrderAsc(maze.getId());
        List<String> imagesToDelete = new ArrayList<>();

        // 기존 문제들의 이미지 경로 수집
        for (MazeQuestion existingQuestion : existingQuestions) {
            if (existingQuestion.getQuestionImage() != null) {
                imagesToDelete.add(existingQuestion.getQuestionImage());
            }
        }

        // 기존 문제들 삭제 (새로 생성할 예정)
        if (!existingQuestions.isEmpty()) {
            mazeQuestionRepository.deleteAll(existingQuestions);
        }

        // 새로운 문제들 생성
        List<MultipartFile> questionImages = updateDto.getQuestionImages();
        List<Long> existingQuestionIds = updateDto.getExistingQuestionIds();

        for (int i = 0; i < updateDto.getCorrectAnswers().size(); i++) {
            MultipartFile questionImage = null;
            if (questionImages != null && i < questionImages.size()) {
                questionImage = questionImages.get(i);
            }
            // 기존 문제인지 새 문제인지 확인
            boolean isExistingQuestion = existingQuestionIds != null &&
                    i < existingQuestionIds.size() &&
                    existingQuestionIds.get(i) != null;
            String questionImagePath = null;
            if (questionImage != null && !questionImage.isEmpty()) {
                // 새로운 이미지가 업로드된 경우
                try {
                    questionImagePath = fileUploadService.saveFile(
                            questionImage,
                            maze.getId(),
                            "question" + (i + 1)
                    );
                } catch (Exception e) {
                    throw new RuntimeException("문제 " + (i + 1) + " 이미지 업로드 중 오류: " + e.getMessage(), e);
                }
            } else if (isExistingQuestion) {
                // 기존 문제이고 새 이미지가 없는 경우, 기존 이미지 경로 찾기
                Long existingQuestionId = existingQuestionIds.get(i);
                for (MazeQuestion existingQuestion : existingQuestions) {
                    if (existingQuestion.getId().equals(existingQuestionId)) {
                        questionImagePath = existingQuestion.getQuestionImage();
                        // 이 이미지는 삭제하지 않도록 리스트에서 제거
                        imagesToDelete.remove(questionImagePath);
                        break;
                    }
                }
            }
            // 새 문제인데 이미지가 없는 경우 에러
            if (!isExistingQuestion && questionImagePath == null) {
                throw new IllegalArgumentException("새로운 문제에는 이미지가 필요합니다.");
            }
            // 기존 문제인데 이미지를 찾을 수 없는 경우 에러
            if (isExistingQuestion && questionImagePath == null) {
                throw new IllegalArgumentException("문제 " + (i + 1) + "의 이미지를 찾을 수 없습니다.");
            }
            // 새 MazeQuestion 생성
            MazeQuestion question = new MazeQuestion();
            question.setMaze(maze);
            question.setQuestionImage(questionImagePath);
            question.setQuestionOrder(i + 1);
            question.setCreatedAt(LocalDateTime.now());
            question.setUpdatedAt(LocalDateTime.now());
            // 다른 필드들 설정
            setQuestionField(question::setTitle, updateDto.getQuestionTitles(), i, "문제 " + (i + 1));
            setQuestionField(question::setCorrectAnswer, updateDto.getCorrectAnswers(), i, "test");
            setQuestionField(question::setHint, updateDto.getHints(), i, null);
            setQuestionNumberField(question::setPoints, updateDto.getPointsList(), i, 10);
            mazeQuestionRepository.save(question);
        }
        // 사용되지 않는 이미지들 삭제
        for (String imageToDelete : imagesToDelete) {
            try {
                fileUploadService.deleteFile(imageToDelete);
            } catch (Exception e) {
                System.err.println("이미지 삭제 실패: " + imageToDelete + " - " + e.getMessage());
            }
        }
    }
    /**
     * User의 Maze확인하는 메소드 혹시 몰라 만들어 놨음
     * @param mazeId 미로 ID
     * @param username 사용자명
     * @return ture or false
     */
    @Transactional(readOnly = true)
    public boolean isMazeOwner(Long mazeId, String username) {
        try {
            MazeDto maze = getMaze(mazeId);
            return maze.getCreatorName().equals(username);
        } catch (Exception e) {
            return false;
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

        // 투표 통계 초기화 또는 설정 (기본 DTO 변환 시)
        dto.initializeEmptyVoteStats();

        return dto;
    }

    private MazeDto convertToDtoWithVoteStats(Maze maze) {
        MazeDto dto = convertToDto(maze);
        if (maze.getStatus() == MazeStatus.APPROVED) {
            long likeCount = mazeVoteRepository.countLikesByMazeId(maze.getId());
            long dislikeCount = mazeVoteRepository.countDislikesByMazeId(maze.getId());
            dto.setVoteStats(likeCount, dislikeCount, null); // userVote는 여기서는 필요 없으므로 null
        }
        return dto;
    }

    /**
     * 안전한 문제 데이터 처리를 위한 검증 메서드
     * Claude 만세!!!
     */
    private void validateUpdateData(MazeUpdateDto updateDto) {
        if (updateDto.getCorrectAnswers() == null || updateDto.getCorrectAnswers().isEmpty()) {
            throw new IllegalArgumentException("최소 1개 이상의 문제가 필요합니다.");
        }

        int questionCount = updateDto.getCorrectAnswers().size();

        for (int i = 0; i < questionCount; i++) {
            String answer = updateDto.getCorrectAnswers().get(i);
            if (answer == null || answer.trim().isEmpty()) {
                throw new IllegalArgumentException("문제 " + (i + 1) + "의 정답을 입력해주세요.");
            }
        }
        System.out.println("수정 데이터 검증 완료: " + questionCount + "개 문제");
    }
}