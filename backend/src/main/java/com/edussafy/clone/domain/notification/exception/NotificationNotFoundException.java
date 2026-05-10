package com.edussafy.clone.domain.notification.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class NotificationNotFoundException extends BusinessException { public NotificationNotFoundException() { super(ErrorCode.NOTIFICATION_NOT_FOUND); } }
