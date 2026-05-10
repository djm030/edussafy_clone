package com.edussafy.clone.domain.learning.dto.mapper;

import com.edussafy.clone.domain.learning.domain.entity.LearningCategory;
import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.dto.response.LearningCategoryResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningContentResponse;
import org.springframework.stereotype.Component;

@Component
public class LearningDtoMapper {
    public LearningCategoryResponse toCategoryResponse(LearningCategory category) {
        return new LearningCategoryResponse(category.getId(),
                category.getParent() == null ? null : category.getParent().getId(),
                category.getName(), category.getCode(), category.getCategoryType());
    }

    public LearningContentResponse toContentResponse(LearningContent content) {
        return new LearningContentResponse(content.getId(),
                content.getCourse() == null ? null : content.getCourse().getId(),
                content.getSession() == null ? null : content.getSession().getId(),
                content.getCategory() == null ? null : content.getCategory().getId(),
                content.getTitle(), content.getDescription(), content.getContentType(),
                content.getThumbnailFile() == null ? null : content.getThumbnailFile().getFileUrl(),
                content.getContentUrl(), content.getDurationSeconds(), content.getViewCount(), content.getLikeCount(),
                content.getBookmarkCount(), content.getDownloadCount(), content.getIsRequired(),
                content.getOpenAt(), content.getCloseAt());
    }
}
