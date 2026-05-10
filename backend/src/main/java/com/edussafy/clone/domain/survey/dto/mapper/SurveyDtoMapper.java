package com.edussafy.clone.domain.survey.dto.mapper;

import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.entity.SurveyCategory;
import com.edussafy.clone.domain.survey.domain.entity.SurveyParticipant;
import com.edussafy.clone.domain.survey.domain.entity.SurveyQuestion;
import com.edussafy.clone.domain.survey.dto.response.SurveyCategoryResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyParticipantResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyQuestionResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SurveyDtoMapper {
    public SurveyCategoryResponse toCategoryResponse(SurveyCategory category) {
        return new SurveyCategoryResponse(category.getId(), category.getName(), category.getCode(), category.getDescription());
    }
    public SurveyQuestionResponse toQuestionResponse(SurveyQuestion question) {
        return new SurveyQuestionResponse(question.getId(), question.getQuestionNo(), question.getQuestionText(), question.getQuestionType(), question.getOptions(), question.getIsRequired(), question.getSortOrder());
    }
    public SurveyResponse toResponse(Survey survey, List<SurveyQuestion> questions) {
        return new SurveyResponse(survey.getId(), survey.getCategory() == null ? null : survey.getCategory().getId(),
                survey.getCategory() == null ? null : survey.getCategory().getName(), survey.getTitle(), survey.getDescription(),
                survey.getFormType(), survey.getOpenAt(), survey.getCloseAt(), survey.getIsRequired(), survey.getEventType(),
                survey.getLocation(), survey.getCapacity(), survey.getSelectionPolicy(), questions.stream().map(this::toQuestionResponse).toList());
    }
    public SurveyParticipantResponse toParticipantResponse(SurveyParticipant participant) {
        return new SurveyParticipantResponse(participant.getId(), participant.getSurvey().getId(), participant.getSurvey().getTitle(),
                participant.getAnswers(), participant.getParticipantStatus(), participant.getSubmittedAt(), participant.getCancelledAt(), participant.getCreatedAt());
    }
}
