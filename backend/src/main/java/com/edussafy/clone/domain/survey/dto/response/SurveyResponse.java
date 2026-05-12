package com.edussafy.clone.domain.survey.dto.response;

import com.edussafy.clone.domain.survey.domain.enums.EventType;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import java.time.LocalDateTime;
import java.util.List;

public record SurveyResponse(Long id, Long categoryId, String categoryName, String title, String description,
                             FormType formType, LocalDateTime openAt, LocalDateTime closeAt, Boolean isRequired,
                             EventType eventType, LocalDateTime eventStartAt, LocalDateTime eventEndAt,
                             String location, Integer capacity, String selectionPolicy,
                             Long linkedPostId, String linkedPostTitle, String linkedPostContent,
                             List<SurveyQuestionResponse> questions) { }
