package com.edussafy.clone.domain.survey.dto.response;

import com.edussafy.clone.domain.survey.domain.enums.QuestionType;

public record SurveyQuestionResponse(Long id, Integer questionNo, String questionText, QuestionType questionType,
                                     String options, Boolean isRequired, Integer sortOrder) { }
