package com.edussafy.clone.global.file;

import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponse> upload(
            @RequestParam MultipartFile file,
            @RequestParam FileTargetType targetType,
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "ATTACHMENT") FileRole fileRole,
            @CurrentUser Long currentUserId
    ) {
        return ApiResponse.ok(fileStorageService.upload(file, currentUserId, targetType, targetId, fileRole));
    }

    @GetMapping("/{fileId}")
    public ApiResponse<FileResponse> getFile(@PathVariable Long fileId) {
        return ApiResponse.ok(fileStorageService.getFile(fileId));
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<?> download(@PathVariable Long fileId) {
        FileStorageService.DownloadFile file = fileStorageService.download(fileId);
        String encodedName = URLEncoder.encode(file.originalName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(file.contentType() == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .body(file.resource());
    }

    @DeleteMapping("/{fileId}")
    public ApiResponse<Void> delete(@PathVariable Long fileId) {
        fileStorageService.delete(fileId);
        return ApiResponse.ok();
    }
}
