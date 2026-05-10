package com.edussafy.clone.domain.course.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class CourseSessionNotFoundException extends BusinessException {
    public CourseSessionNotFoundException() { super(ErrorCode.COURSE_SESSION_NOT_FOUND); }
}
