package com.edussafy.clone.domain.task.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TaskSubmitRequest(@NotNull Map<String, Object> answerData) { }
