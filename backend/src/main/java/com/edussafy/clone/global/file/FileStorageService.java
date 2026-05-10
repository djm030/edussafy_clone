package com.edussafy.clone.global.file;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class FileStorageService {

    private final FileResourceRepository fileResourceRepository;
    private final UserRepository userRepository;

    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    public FileUploadResponse upload(MultipartFile multipartFile, Long uploadedById, FileTargetType targetType,
                                     Long targetId, FileRole fileRole) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new FileStorageException();
        }
        String originalName = StringUtils.cleanPath(multipartFile.getOriginalFilename() == null
                ? "file"
                : multipartFile.getOriginalFilename());
        String extension = extensionOf(originalName);
        String storedName = UUID.randomUUID() + extension;
        Path targetPath = uploadRoot().resolve(storedName);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.createDirectories(uploadRoot());
            Files.copy(inputStream, targetPath);
        } catch (IOException e) {
            throw new FileStorageException();
        }
        User uploadedBy = uploadedById == null ? null : userRepository.findById(uploadedById).orElse(null);
        FileResource saved = fileResourceRepository.save(FileResource.builder()
                .originalName(originalName)
                .storedName(storedName)
                .fileUrl("/api/v1/files/" + storedName + "/raw")
                .fileType(multipartFile.getContentType())
                .fileSize(multipartFile.getSize())
                .uploadedBy(uploadedBy)
                .targetType(targetType)
                .targetId(targetId)
                .fileRole(fileRole == null ? FileRole.ATTACHMENT : fileRole)
                .sortOrder(0)
                .build());
        return new FileUploadResponse(saved.getId(), saved.getOriginalName(), saved.getFileUrl(), saved.getFileType(),
                saved.getFileSize(), saved.getTargetType(), saved.getTargetId(), saved.getFileRole());
    }

    @Transactional(readOnly = true)
    public FileResponse getFile(Long fileId) {
        FileResource file = fileResourceRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        return toResponse(file);
    }

    @Transactional(readOnly = true)
    public DownloadFile download(Long fileId) {
        FileResource file = fileResourceRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        Path path = uploadRoot().resolve(file.getStoredName()).normalize();
        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException();
            }
            return new DownloadFile(resource, file.getOriginalName(), file.getFileType());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException();
        }
    }

    public void delete(Long fileId) {
        FileResource file = fileResourceRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        try {
            Files.deleteIfExists(uploadRoot().resolve(file.getStoredName()).normalize());
        } catch (IOException ignored) {
            // DB 기준 삭제는 계속 진행한다. 실제 파일 정리는 운영 스토리지 정책에서 보완한다.
        }
        fileResourceRepository.delete(file);
    }

    private FileResponse toResponse(FileResource file) {
        return new FileResponse(file.getId(), file.getOriginalName(), file.getFileUrl(), file.getFileType(),
                file.getFileSize(), file.getTargetType(), file.getTargetId(), file.getFileRole(), file.getSortOrder());
    }

    private Path uploadRoot() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    private String extensionOf(String originalName) {
        int idx = originalName.lastIndexOf('.');
        return idx >= 0 ? originalName.substring(idx) : "";
    }

    public record DownloadFile(Resource resource, String originalName, String contentType) {
    }
}
