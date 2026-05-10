package com.edussafy.clone.domain.learning.dto.request;

import com.edussafy.clone.domain.learning.domain.enums.ContentInteractionType;
import jakarta.validation.constraints.NotNull;

public record LearningInteractionRequest(@NotNull ContentInteractionType interactionType) {
}
