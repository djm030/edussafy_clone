package com.edussafy.clone.domain.board.dto.request;

import com.edussafy.clone.domain.board.application.command.UpdateBoardCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record BoardCommentUpdateRequest(@NotBlank(message = "댓글 내용을 입력하세요.") String content) {
    public UpdateBoardCommentCommand toCommand() {
        return new UpdateBoardCommentCommand(content);
    }
}
