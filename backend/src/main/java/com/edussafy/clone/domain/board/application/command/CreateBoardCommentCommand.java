package com.edussafy.clone.domain.board.application.command;

public record CreateBoardCommentCommand(Long parentId, String content) {
}
