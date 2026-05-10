package com.edussafy.clone.domain.board.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class BoardAccessDeniedException extends BusinessException {
    public BoardAccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
