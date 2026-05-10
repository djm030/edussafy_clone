package com.edussafy.clone.domain.task.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class CourseTaskNotFoundException extends BusinessException { public CourseTaskNotFoundException() { super(ErrorCode.COURSE_TASK_NOT_FOUND); } }
