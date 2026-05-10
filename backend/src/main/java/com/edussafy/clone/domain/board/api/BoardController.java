package com.edussafy.clone.domain.board.api;

import com.edussafy.clone.domain.board.application.BoardCommandService;
import com.edussafy.clone.domain.board.application.BoardQueryService;
import com.edussafy.clone.domain.board.dto.request.BoardPostCreateRequest;
import com.edussafy.clone.domain.board.dto.request.BoardPostUpdateRequest;
import com.edussafy.clone.domain.board.dto.response.BoardCategoryResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostCreateResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostDetailResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostListResponse;
import com.edussafy.clone.domain.board.dto.response.BoardResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import com.edussafy.clone.global.security.CurrentUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardQueryService boardQueryService;
    private final BoardCommandService boardCommandService;

    @GetMapping
    public ApiResponse<List<BoardResponse>> getBoards() {
        return ApiResponse.ok(boardQueryService.getBoards());
    }

    @GetMapping("/{boardCode}/categories")
    public ApiResponse<List<BoardCategoryResponse>> getCategories(@PathVariable String boardCode) {
        return ApiResponse.ok(boardQueryService.getCategories(boardCode));
    }

    @GetMapping("/{boardCode}/posts")
    public ApiResponse<PageResponse<BoardPostListResponse>> getPosts(
            @PathVariable String boardCode,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @CurrentUser Long currentUserId
    ) {
        return ApiResponse.ok(boardQueryService.getPosts(boardCode, categoryId, keyword, page, size, currentUserId));
    }

    @GetMapping("/{boardCode}/posts/{postId}")
    public ApiResponse<BoardPostDetailResponse> getPost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser Long currentUserId
    ) {
        return ApiResponse.ok(boardQueryService.getPost(boardCode, postId, currentUserId));
    }

    @PostMapping("/{boardCode}/posts")
    public ApiResponse<BoardPostCreateResponse> createPost(
            @PathVariable String boardCode,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardPostCreateRequest request
    ) {
        Long postId = boardCommandService.createPost(boardCode, currentUser.userId(), currentUser.role(), request.toCommand());
        return ApiResponse.ok(new BoardPostCreateResponse(postId));
    }

    @PatchMapping("/{boardCode}/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardPostUpdateRequest request
    ) {
        boardCommandService.updatePost(boardCode, postId, currentUser.userId(), currentUser.role(), request.toCommand());
        return ApiResponse.ok();
    }

    @DeleteMapping("/{boardCode}/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser CurrentUserPrincipal currentUser
    ) {
        boardCommandService.deletePost(boardCode, postId, currentUser.userId(), currentUser.role());
        return ApiResponse.ok();
    }
}
