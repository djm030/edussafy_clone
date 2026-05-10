package com.edussafy.clone.domain.board.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class BoardPostNotFoundException extends BusinessException {
    public BoardPostNotFoundException() {
        super(ErrorCode.BOARD_POST_NOT_FOUND);
    }
}
