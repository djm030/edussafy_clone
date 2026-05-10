package com.edussafy.clone.domain.agreement.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class AgreementNotFoundException extends BusinessException { public AgreementNotFoundException() { super(ErrorCode.AGREEMENT_NOT_FOUND); } }
