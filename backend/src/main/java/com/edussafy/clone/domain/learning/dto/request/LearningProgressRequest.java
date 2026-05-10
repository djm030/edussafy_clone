package com.edussafy.clone.domain.learning.dto.request;

import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record LearningProgressRequest(
        @DecimalMin("0.0") @DecimalMax("100.0") Double progressRate,
        Integer lastPositionSeconds,
        LearningProgressStatus progressStatus
) {
}
