package com.edussafy.clone.domain.learning.dto.response;

import com.edussafy.clone.domain.learning.domain.enums.LearningCategoryType;

public record LearningCategoryResponse(Long id, Long parentId, String name, String code, LearningCategoryType categoryType) {
}
