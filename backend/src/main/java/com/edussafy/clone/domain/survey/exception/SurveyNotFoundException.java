package com.edussafy.clone.domain.survey.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class SurveyNotFoundException extends BusinessException { public SurveyNotFoundException() { super(ErrorCode.SURVEY_NOT_FOUND); } }
