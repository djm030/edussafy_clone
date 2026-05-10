package com.edussafy.clone.domain.learning.application;

import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import com.edussafy.clone.domain.learning.domain.repository.LearningCategoryRepository;
import com.edussafy.clone.domain.learning.domain.repository.LearningContentRepository;
import com.edussafy.clone.domain.learning.dto.mapper.LearningDtoMapper;
import com.edussafy.clone.domain.learning.dto.response.LearningCategoryResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningContentResponse;
import com.edussafy.clone.domain.learning.exception.LearningContentNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningQueryService {
    private final LearningCategoryRepository learningCategoryRepository;
    private final LearningContentRepository learningContentRepository;
    private final LearningDtoMapper learningDtoMapper;

    public List<LearningCategoryResponse> getCategories() {
        return learningCategoryRepository.findAllByOrderByIdAsc().stream()
                .map(learningDtoMapper::toCategoryResponse)
                .toList();
    }

    public PageResponse<LearningContentResponse> getContents(Long categoryId, Long courseId, String keyword,
                                                             LearningContentType contentType, boolean requiredOnly,
                                                             int page, int size) {
        Page<LearningContentResponse> contents = learningContentRepository
                .search(categoryId, courseId, contentType, blankToNull(keyword), requiredOnly, PageRequest.of(page, size))
                .map(learningDtoMapper::toContentResponse);
        return PageResponse.from(contents);
    }

    @Transactional
    public LearningContentResponse getContent(Long contentId) {
        LearningContent content = learningContentRepository.findById(contentId)
                .orElseThrow(LearningContentNotFoundException::new);
        content.increaseViewCount();
        return learningDtoMapper.toContentResponse(content);
    }

    private String blankToNull(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword;
    }
}
