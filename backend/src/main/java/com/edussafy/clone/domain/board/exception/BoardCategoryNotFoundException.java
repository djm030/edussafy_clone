package com.edussafy.clone.domain.board.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class BoardCategoryNotFoundException extends BusinessException {
    public BoardCategoryNotFoundException() {
        super(ErrorCode.BOARD_CATEGORY_NOT_FOUND);
    }
}
