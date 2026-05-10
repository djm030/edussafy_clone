package com.edussafy.clone.domain.attendance.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class AttendanceAppealNotFoundException extends BusinessException { public AttendanceAppealNotFoundException() { super(ErrorCode.ATTENDANCE_APPEAL_NOT_FOUND); } }
