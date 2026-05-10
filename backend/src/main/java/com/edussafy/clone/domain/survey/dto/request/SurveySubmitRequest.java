package com.edussafy.clone.domain.survey.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record SurveySubmitRequest(@NotNull Map<String, Object> answers) { }
