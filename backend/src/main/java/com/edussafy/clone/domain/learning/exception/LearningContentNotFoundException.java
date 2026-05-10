package com.edussafy.clone.domain.learning.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class LearningContentNotFoundException extends BusinessException {
    public LearningContentNotFoundException() { super(ErrorCode.LEARNING_CONTENT_NOT_FOUND); }
}
