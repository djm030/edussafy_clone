package com.edussafy.clone.domain.board.application;

import com.edussafy.clone.domain.board.dto.response.BoardFileResponse;
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.file.FileRole;
import com.edussafy.clone.global.file.FileTargetType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardAttachmentService {

    private final FileResourceRepository fileResourceRepository;

    public void linkPostFiles(Long postId, List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return;
        }
        List<FileResource> files = fileResourceRepository.findByIdIn(fileIds);
        for (int i = 0; i < files.size(); i++) {
            files.get(i).linkTo(FileTargetType.BOARD_POST, postId, FileRole.ATTACHMENT, i);
        }
    }

    @Transactional(readOnly = true)
    public List<BoardFileResponse> getPostFiles(Long postId) {
        return fileResourceRepository
                .findByTargetTypeAndTargetIdAndFileRoleOrderBySortOrderAscIdAsc(
                        FileTargetType.BOARD_POST,
                        postId,
                        FileRole.ATTACHMENT
                )
                .stream()
                .map(file -> new BoardFileResponse(
                        file.getId(),
                        file.getOriginalName(),
                        file.getFileUrl(),
                        file.getFileType(),
                        file.getFileSize(),
                        file.getSortOrder()
                ))
                .toList();
    }
}
