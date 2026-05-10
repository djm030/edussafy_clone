package com.edussafy.clone.domain.board.dto.request;

import com.edussafy.clone.domain.board.application.command.CreateBoardCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record BoardCommentCreateRequest(
        Long parentId,
        @NotBlank(message = "댓글 내용을 입력하세요.") String content
) {
    public CreateBoardCommentCommand toCommand() {
        return new CreateBoardCommentCommand(parentId, content);
    }
}
