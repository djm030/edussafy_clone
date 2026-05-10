package com.edussafy.clone.domain.bookmark.api;

import com.edussafy.clone.domain.bookmark.application.BookmarkService;
import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.bookmark.dto.request.BookmarkRequest;
import com.edussafy.clone.domain.bookmark.dto.response.BookmarkResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<BookmarkResponse>> getMyBookmarks(@CurrentUser Long currentUserId,
            @RequestParam(required = false) BookmarkTargetType targetType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(bookmarkService.getMyBookmarks(currentUserId, targetType, page, size));
    }

    @PostMapping
    public ApiResponse<BookmarkResponse> addBookmark(@CurrentUser Long currentUserId, @Valid @RequestBody BookmarkRequest request) {
        return ApiResponse.ok(bookmarkService.addBookmark(currentUserId, request));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteBookmark(@CurrentUser Long currentUserId, @Valid @RequestBody BookmarkRequest request) {
        bookmarkService.deleteBookmark(currentUserId, request);
        return ApiResponse.ok(null);
    }
}
