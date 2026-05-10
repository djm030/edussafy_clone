package com.edussafy.clone.domain.survey.dto.response;

import com.edussafy.clone.domain.survey.domain.enums.ParticipantStatus;
import java.time.LocalDateTime;

public record SurveyParticipantResponse(Long id, Long surveyId, String surveyTitle, String answers,
                                        ParticipantStatus participantStatus, LocalDateTime submittedAt,
                                        LocalDateTime cancelledAt, LocalDateTime createdAt) { }
