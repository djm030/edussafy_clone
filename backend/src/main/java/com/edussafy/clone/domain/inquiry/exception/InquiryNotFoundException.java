package com.edussafy.clone.domain.inquiry.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class InquiryNotFoundException extends BusinessException { public InquiryNotFoundException() { super(ErrorCode.INQUIRY_NOT_FOUND); } }
