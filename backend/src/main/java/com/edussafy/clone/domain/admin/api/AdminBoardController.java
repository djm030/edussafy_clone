package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminBoardService;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminBoardController {
    private final AdminBoardService service;

    @PostMapping("/boards")
    public ApiResponse<Map<String, Object>> createBoard(@CurrentUser Long adminId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.createBoard(adminId, request));
    }

    @PatchMapping("/boards/{boardId}")
    public ApiResponse<Map<String, Object>> updateBoard(@CurrentUser Long adminId, @PathVariable Long boardId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.updateBoard(adminId, boardId, request));
    }

    @PostMapping("/boards/{boardId}/categories")
    public ApiResponse<Map<String, Object>> createCategory(@CurrentUser Long adminId, @PathVariable Long boardId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.createCategory(adminId, boardId, request));
    }

    @PatchMapping("/board-categories/{categoryId}")
    public ApiResponse<Map<String, Object>> updateCategory(@CurrentUser Long adminId, @PathVariable Long categoryId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.updateCategory(adminId, categoryId, request));
    }

    @DeleteMapping("/board-categories/{categoryId}")
    public ApiResponse<Void> deleteCategory(@CurrentUser Long adminId, @PathVariable Long categoryId) {
        service.deleteCategory(adminId, categoryId);
        return ApiResponse.ok();
    }

    @PatchMapping("/board-posts/{postId}/delete")
    public ApiResponse<Void> deletePost(@CurrentUser Long adminId, @PathVariable Long postId) {
        service.deletePost(adminId, postId);
        return ApiResponse.ok();
    }

    @PatchMapping("/board-posts/{postId}/restore")
    public ApiResponse<Void> restorePost(@CurrentUser Long adminId, @PathVariable Long postId) {
        service.restorePost(adminId, postId);
        return ApiResponse.ok();
    }
}
