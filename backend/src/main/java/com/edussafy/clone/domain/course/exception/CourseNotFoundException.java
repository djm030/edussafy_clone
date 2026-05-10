package com.edussafy.clone.domain.course.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class CourseNotFoundException extends BusinessException {
    public CourseNotFoundException() { super(ErrorCode.COURSE_NOT_FOUND); }
}
