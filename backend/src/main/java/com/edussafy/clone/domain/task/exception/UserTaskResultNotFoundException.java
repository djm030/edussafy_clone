package com.edussafy.clone.domain.task.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class UserTaskResultNotFoundException extends BusinessException { public UserTaskResultNotFoundException() { super(ErrorCode.USER_TASK_RESULT_NOT_FOUND); } }
