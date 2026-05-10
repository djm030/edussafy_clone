package com.edussafy.clone.domain.learning.api;

import com.edussafy.clone.domain.bookmark.application.BookmarkService;
import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.bookmark.dto.response.BookmarkResponse;
import com.edussafy.clone.domain.learning.application.LearningInteractionService;
import com.edussafy.clone.domain.learning.application.LearningProgressService;
import com.edussafy.clone.domain.learning.application.LearningQueryService;
import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import com.edussafy.clone.domain.learning.dto.request.LearningInteractionRequest;
import com.edussafy.clone.domain.learning.dto.request.LearningProgressRequest;
import com.edussafy.clone.domain.learning.dto.response.LearningCategoryResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningContentResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningInteractionResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningProgressResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/learning")
public class LearningController {
    private final LearningQueryService learningQueryService;
    private final LearningInteractionService learningInteractionService;
    private final LearningProgressService learningProgressService;
    private final BookmarkService bookmarkService;

    @GetMapping("/categories")
    public ApiResponse<List<LearningCategoryResponse>> getCategories() {
        return ApiResponse.ok(learningQueryService.getCategories());
    }

    @GetMapping("/contents")
    public ApiResponse<PageResponse<LearningContentResponse>> getContents(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LearningContentType contentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(learningQueryService.getContents(categoryId, courseId, keyword, contentType, false, page, size));
    }

    @GetMapping("/contents/required")
    public ApiResponse<PageResponse<LearningContentResponse>> getRequiredContents(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(learningQueryService.getContents(categoryId, courseId, null, null, true, page, size));
    }

    @GetMapping("/contents/open-learning")
    public ApiResponse<PageResponse<LearningContentResponse>> getOpenLearningContents(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LearningContentType contentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(learningQueryService.getContents(categoryId, null, keyword, contentType, false, page, size));
    }

    @GetMapping("/contents/{contentId}")
    public ApiResponse<LearningContentResponse> getContent(@PathVariable Long contentId) {
        return ApiResponse.ok(learningQueryService.getContent(contentId));
    }

    @PostMapping("/contents/{contentId}/like")
    public ApiResponse<LearningInteractionResponse> like(@PathVariable Long contentId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(learningInteractionService.like(contentId, currentUserId));
    }

    @PostMapping("/contents/{contentId}/download")
    public ApiResponse<LearningInteractionResponse> download(@PathVariable Long contentId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(learningInteractionService.download(contentId, currentUserId));
    }

    @PostMapping("/contents/{contentId}/progress")
    public ApiResponse<LearningProgressResponse> saveProgress(
            @PathVariable Long contentId,
            @CurrentUser Long currentUserId,
            @RequestBody LearningProgressRequest request
    ) {
        return ApiResponse.ok(learningProgressService.saveProgress(contentId, currentUserId, request));
    }

    @PostMapping("/contents/{contentId}/complete")
    public ApiResponse<LearningProgressResponse> complete(@PathVariable Long contentId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(learningProgressService.complete(contentId, currentUserId));
    }

    @GetMapping("/progress/my")
    public ApiResponse<PageResponse<LearningProgressResponse>> getMyProgress(
            @CurrentUser Long currentUserId,
            @RequestParam(required = false) LearningProgressStatus progressStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(learningProgressService.getMyProgress(currentUserId, progressStatus, page, size));
    }

    @GetMapping("/contents/my-knowledge")
    public ApiResponse<PageResponse<LearningProgressResponse>> getMyKnowledge(
            @CurrentUser Long currentUserId,
            @RequestParam(required = false) LearningProgressStatus progressStatus,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(learningProgressService.getMyProgress(currentUserId, progressStatus, page, size));
    }

    @GetMapping("/contents/my-selected")
    public ApiResponse<PageResponse<BookmarkResponse>> getMySelectedContents(
            @CurrentUser Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(bookmarkService.getMyBookmarks(currentUserId, BookmarkTargetType.LEARNING_CONTENT, page, size));
    }

    @PostMapping("/contents/{contentId}/interactions")
    public ApiResponse<LearningInteractionResponse> recordInteraction(
            @PathVariable Long contentId,
            @CurrentUser Long currentUserId,
            @RequestBody LearningInteractionRequest request
    ) {
        return ApiResponse.ok(learningInteractionService.record(contentId, currentUserId, request.interactionType()));
    }
}
