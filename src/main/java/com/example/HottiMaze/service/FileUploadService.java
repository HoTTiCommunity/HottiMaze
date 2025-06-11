package com.example.HottiMaze.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class FileUploadService {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    @Value("${gcp.credential-path}")
    private String credentialPath;

    @Value("${gcp.image-base-url}")
    private String gcsImageBaseUrl;

    private Storage storage;

    @PostConstruct
    public void init() throws IOException {
        InputStream credentialsStream;
        if (credentialPath.startsWith("classpath:")) {
            String resourcePath = credentialPath.substring("classpath:".length());
            ClassPathResource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                throw new IOException("Classpath resource not found: " + resourcePath + " in project " + projectId + ".");
            }
            credentialsStream = resource.getInputStream();
        } else {
            credentialsStream = new FileInputStream(credentialPath);
        }
        storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build()
                .getService();
    }
    public String saveFile(MultipartFile file, Long mazeId, String filePrefix) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 가능)");
        }

        String fileExtension = getFileExtension(originalFilename);
        String gcsFileName = "mazes/" + "maze" + mazeId + "/" + filePrefix + fileExtension; // 변경된 부분

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsFileName).build();
        storage.create(blobInfo, file.getBytes());

        String publicUrl = gcsImageBaseUrl + gcsFileName;
        System.out.println("File uploaded to GCS: " + publicUrl);
        return publicUrl;
    }

    public String renameFile(String oldFileUrl, Long oldMazeId, Long newMazeId) throws IOException {
        if (oldFileUrl == null || !oldFileUrl.startsWith(gcsImageBaseUrl)) {
            System.out.println("유효하지 않은 이전 파일 URL: " + oldFileUrl);
            return null;
        }
        String oldGcsFileName = oldFileUrl.substring(gcsImageBaseUrl.length());

        String filenameOnly = oldGcsFileName.substring(oldGcsFileName.lastIndexOf("/") + 1);
        String newGcsFileName = "mazes/" + "maze" + newMazeId + "/" + filenameOnly; // 변경된 부분

        try {
            BlobId sourceBlobId = BlobId.of(bucketName, oldGcsFileName);
            Blob sourceBlob = storage.get(sourceBlobId);

            if (sourceBlob == null) {
                System.out.println("GCS에서 이전 파일을 찾을 수 없음: " + oldFileUrl);
                return null;
            }

            sourceBlob.copyTo(bucketName, newGcsFileName);

            storage.delete(sourceBlobId);

            String newPublicUrl = gcsImageBaseUrl + newGcsFileName;
            System.out.println("File renamed from " + oldFileUrl + " to " + newPublicUrl + " in GCS.");
            return newPublicUrl;

        } catch (Exception e) {
            System.err.println("GCS 파일 이름 변경 실패: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("파일 이름 변경 중 오류 발생", e);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(gcsImageBaseUrl)) {
            System.out.println("Invalid file URL for GCS deletion: " + fileUrl);
            return;
        }
        String gcsFileName = fileUrl.substring(gcsImageBaseUrl.length());
        storage.delete(bucketName, gcsFileName);
        System.out.println("File deleted from GCS: " + fileUrl);
    }

    public void deleteMazeFolder(Long mazeId) {
        String folderPrefix = "mazes/" + "maze" + mazeId + "/"; // 변경된 부분
        System.out.println("=== 미로 폴더 삭제 시작 (GCS) ===");
        System.out.println("삭제 대상 폴더 접두사: " + folderPrefix);

        try {
            Iterable<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(folderPrefix)).iterateAll();
            boolean foundFiles = false;
            for (BlobInfo blob : blobs) {
                storage.delete(blob.getBlobId());
                System.out.println("Blob 삭제: " + blob.getName());
                foundFiles = true;
            }
            if (!foundFiles) {
                System.out.println("삭제할 파일이 없음.");
            }
            System.out.println("폴더 " + folderPrefix + "의 모든 파일이 GCS에서 삭제되었습니다.");
            System.out.println("========================");
        } catch (Exception e) {
            System.err.println("미로 폴더 삭제 실패: " + folderPrefix + " - " + e.getMessage());
            e.printStackTrace();
        }
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
}