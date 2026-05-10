package com.edussafy.clone.domain.board.dto.request;

import com.edussafy.clone.domain.board.application.command.CreateBoardPostCommand;
import com.edussafy.clone.domain.board.domain.enums.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BoardPostCreateRequest(
        @NotNull(message = "카테고리를 선택하세요.") Long categoryId,
        @NotBlank(message = "제목을 입력하세요.") String title,
        @NotNull(message = "본문 타입을 선택하세요.") ContentType contentType,
        String contentText,
        String contentHtml,
        String contentJson,
        List<Long> fileIds
) {
    public CreateBoardPostCommand toCommand() {
        return new CreateBoardPostCommand(categoryId, title, contentType, contentText, contentHtml, contentJson, fileIds == null ? List.of() : fileIds);
    }
}
