package com.edussafy.clone.domain.board.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class BoardCommentNotFoundException extends BusinessException {
    public BoardCommentNotFoundException() {
        super(ErrorCode.BOARD_COMMENT_NOT_FOUND);
    }
}
