package com.edussafy.clone.domain.attendance.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class AttendanceRecordNotFoundException extends BusinessException { public AttendanceRecordNotFoundException() { super(ErrorCode.ATTENDANCE_RECORD_NOT_FOUND); } }
