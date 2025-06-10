package com.example.HottiMaze.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileUploadService {

    @Value("${file.upload-dir:src/main/resources/static/imgs/mazes}")
    private String uploadDir;

    public String saveFile(MultipartFile file, Long mazeId, String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 파일 확장자 검증
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (!isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 가능)");
        }

        // maze{id} 형태의 폴더명 생성
        String mazeFolder = "maze" + mazeId;

        // 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir, mazeFolder);

        // 디버깅 로그 추가
        System.out.println("=== 파일 업로드 디버깅 ===");
        System.out.println("uploadDir: " + uploadDir);
        System.out.println("mazeFolder: " + mazeFolder);
        System.out.println("uploadPath: " + uploadPath.toAbsolutePath());
        System.out.println("fileName: " + fileName);
        System.out.println("originalFilename: " + originalFilename);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("디렉토리 생성됨: " + uploadPath.toAbsolutePath());
        }

        // 파일명 생성
        String fileExtension = getFileExtension(originalFilename);
        String savedFileName = fileName + fileExtension;

        // 파일 저장
        Path filePath = uploadPath.resolve(savedFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("파일 저장됨: " + filePath.toAbsolutePath());

        // 웹 경로 반환
        String webPath = "/static/imgs/mazes/" + mazeFolder + "/" + savedFileName;
        System.out.println("반환된 웹 경로: " + webPath);
        System.out.println("========================");

        return webPath;
    }

    // renameFile 메서드 추가
    public String renameFile(String oldFilePath, Long oldMazeId, Long newMazeId) throws IOException {
        // 기존 파일 경로 파싱
        String fileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
        String oldMazeFolder = "maze" + oldMazeId;
        String newMazeFolder = "maze" + newMazeId;

        // 기존 폴더와 새 폴더 경로
        Path oldFolderPath = Paths.get(uploadDir, oldMazeFolder);
        Path newFolderPath = Paths.get(uploadDir, newMazeFolder);

        // 새 폴더 생성
        if (!Files.exists(newFolderPath)) {
            Files.createDirectories(newFolderPath);
        }

        // 파일 이동
        Path oldFile = oldFolderPath.resolve(fileName);
        Path newFile = newFolderPath.resolve(fileName);

        if (Files.exists(oldFile)) {
            Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);

            // 기존 폴더가 비어있으면 삭제
            if (Files.exists(oldFolderPath) && Files.list(oldFolderPath).count() == 0) {
                Files.delete(oldFolderPath);
            }
        }

        // 새 웹 경로 반환
        return "/static/imgs/mazes/" + newMazeFolder + "/" + fileName;
    }

    public String getMazeFolderName(Long mazeId) {
        return "maze" + mazeId;
    }

    private boolean isValidImageFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        return extension.equals(".jpg") || extension.equals(".jpeg") ||
                extension.equals(".png") || extension.equals(".gif");
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex);
    }

    // 단일 파일 삭제
    public void deleteFile(String filePath) {
        try {
            // 웹 경로를 실제 파일 경로로 변환
            String actualPath = filePath.replace("/static/imgs/mazes/", "");
            Path fileToDelete = Paths.get(uploadDir, actualPath);

            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
                System.out.println("파일 삭제 완료: " + fileToDelete.toAbsolutePath());
            } else {
                System.out.println("삭제할 파일이 존재하지 않음: " + fileToDelete.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("파일 삭제 실패: " + filePath + " - " + e.getMessage());
        }
    }

    // 미로 폴더 전체 삭제 (강화된 버전)
    public void deleteMazeFolder(Long mazeId) {
        try {
            String mazeFolder = getMazeFolderName(mazeId);
            Path folderToDelete = Paths.get(uploadDir, mazeFolder);

            System.out.println("=== 미로 폴더 삭제 시작 ===");
            System.out.println("삭제 대상 폴더: " + folderToDelete.toAbsolutePath());

            // 폴더가 존재하는지 확인
            if (!Files.exists(folderToDelete)) {
                System.out.println("삭제할 폴더가 존재하지 않음: " + folderToDelete.toAbsolutePath());
                return;
            }

            // 폴더 내 모든 파일과 하위 폴더를 재귀적으로 삭제
            try (Stream<Path> pathStream = Files.walk(folderToDelete)) {
                pathStream.sorted((a, b) -> -a.compareTo(b)) // 역순으로 정렬하여 하위 요소부터 삭제
                        .forEach(path -> {
                            try {
                                System.out.println("삭제 중: " + path.toAbsolutePath());
                                Files.delete(path);
                                System.out.println("삭제 완료: " + path.toAbsolutePath());
                            } catch (IOException e) {
                                System.err.println("개별 파일/폴더 삭제 실패: " + path.toAbsolutePath() + " - " + e.getMessage());
                            }
                        });
            }

            System.out.println("미로 폴더 삭제 완료: " + folderToDelete.toAbsolutePath());
            System.out.println("========================");

        } catch (IOException e) {
            System.err.println("미로 폴더 삭제 실패: maze" + mazeId + " - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("예상치 못한 오류로 미로 폴더 삭제 실패: maze" + mazeId + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 특정 미로의 모든 파일 경로 조회 (디버깅/검증용)
    public void listMazeFiles(Long mazeId) {
        try {
            String mazeFolder = getMazeFolderName(mazeId);
            Path folderPath = Paths.get(uploadDir, mazeFolder);

            System.out.println("=== 미로 " + mazeId + " 파일 목록 ===");
            System.out.println("폴더 경로: " + folderPath.toAbsolutePath());

            if (!Files.exists(folderPath)) {
                System.out.println("폴더가 존재하지 않음");
                return;
            }

            try (Stream<Path> pathStream = Files.walk(folderPath)) {
                pathStream.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                long fileSize = Files.size(path);
                                System.out.println("파일: " + path.getFileName() + " (크기: " + fileSize + " bytes)");
                            } catch (IOException e) {
                                System.err.println("파일 정보 조회 실패: " + path.getFileName());
                            }
                        });
            }

            System.out.println("========================");

        } catch (IOException e) {
            System.err.println("미로 파일 목록 조회 실패: maze" + mazeId + " - " + e.getMessage());
        }
    }

    // 전체 업로드 디렉토리 정리 (관리용)
    public void cleanupOrphanedFolders() {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                System.out.println("업로드 디렉토리가 존재하지 않음: " + uploadPath.toAbsolutePath());
                return;
            }

            System.out.println("=== 고아 폴더 정리 시작 ===");

            try (Stream<Path> pathStream = Files.list(uploadPath)) {
                pathStream.filter(Files::isDirectory)
                        .filter(path -> path.getFileName().toString().startsWith("maze"))
                        .forEach(path -> {
                            try {
                                // 폴더가 비어있는지 확인
                                try (Stream<Path> folderContents = Files.list(path)) {
                                    if (folderContents.count() == 0) {
                                        Files.delete(path);
                                        System.out.println("빈 폴더 삭제: " + path.getFileName());
                                    }
                                }
                            } catch (IOException e) {
                                System.err.println("폴더 정리 실패: " + path.getFileName() + " - " + e.getMessage());
                            }
                        });
            }

            System.out.println("고아 폴더 정리 완료");
            System.out.println("========================");

        } catch (IOException e) {
            System.err.println("고아 폴더 정리 중 오류 발생: " + e.getMessage());
        }
    }
}