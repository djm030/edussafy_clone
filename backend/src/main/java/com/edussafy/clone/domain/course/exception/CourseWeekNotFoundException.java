package com.edussafy.clone.domain.course.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class CourseWeekNotFoundException extends BusinessException {
    public CourseWeekNotFoundException() { super(ErrorCode.COURSE_WEEK_NOT_FOUND); }
}
