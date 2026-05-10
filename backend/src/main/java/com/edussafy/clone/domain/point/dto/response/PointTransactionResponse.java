package com.edussafy.clone.domain.point.dto.response;

import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import java.time.LocalDateTime;

public record PointTransactionResponse(Long id, PointTransactionType transactionType, Integer pointAmount,
                                       Integer expAmount, String reason, String targetType, Long targetId,
                                       LocalDateTime createdAt) { }
