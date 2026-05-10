package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import jakarta.validation.constraints.NotNull;

public record AdminPointAdjustRequest(@NotNull Long userId, PointTransactionType transactionType, Integer pointAmount, Integer expAmount, String reason) {
}
