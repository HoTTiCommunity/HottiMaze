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
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일명 생성
        String fileExtension = getFileExtension(originalFilename);
        String savedFileName = fileName + fileExtension;

        // 파일 저장
        Path filePath = uploadPath.resolve(savedFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 웹 경로 반환
        return "/static/imgs/mazes/" + mazeFolder + "/" + savedFileName;
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

    public void deleteFile(String filePath) {
        try {
            // 웹 경로를 실제 파일 경로로 변환
            String actualPath = filePath.replace("/static/imgs/mazes/", "");
            Path fileToDelete = Paths.get(uploadDir, actualPath);
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            System.err.println("파일 삭제 실패: " + filePath + " - " + e.getMessage());
        }
    }

    public void deleteMazeFolder(Long mazeId) {
        try {
            String mazeFolder = getMazeFolderName(mazeId);
            Path folderToDelete = Paths.get(uploadDir, mazeFolder);

            // 폴더 내 모든 파일 삭제 후 폴더 삭제
            if (Files.exists(folderToDelete)) {
                Files.walk(folderToDelete)
                        .sorted((a, b) -> -a.compareTo(b)) // 역순으로 정렬하여 하위 요소부터 삭제
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("파일/폴더 삭제 실패: " + path + " - " + e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            System.err.println("미로 폴더 삭제 실패: maze" + mazeId + " - " + e.getMessage());
        }
    }
}