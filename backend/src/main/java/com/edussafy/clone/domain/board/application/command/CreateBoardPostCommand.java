package com.edussafy.clone.domain.board.application.command;

import com.edussafy.clone.domain.board.domain.enums.ContentType;
import java.util.List;

public record CreateBoardPostCommand(
        Long categoryId,
        String title,
        ContentType contentType,
        String contentText,
        String contentHtml,
        String contentJson,
        List<Long> fileIds
) {
}
