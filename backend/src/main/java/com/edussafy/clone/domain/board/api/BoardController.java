package com.edussafy.clone.domain.board.api;

import com.edussafy.clone.domain.board.application.BoardCommandService;
import com.edussafy.clone.domain.board.application.BoardCommentService;
import com.edussafy.clone.domain.board.application.BoardQueryService;
import com.edussafy.clone.domain.board.dto.request.BoardCommentCreateRequest;
import com.edussafy.clone.domain.board.dto.request.BoardCommentUpdateRequest;
import com.edussafy.clone.domain.board.dto.request.BoardPostCreateRequest;
import com.edussafy.clone.domain.board.dto.request.BoardPostUpdateRequest;
import com.edussafy.clone.domain.board.dto.response.BoardCategoryResponse;
import com.edussafy.clone.domain.board.dto.response.BoardCommentCreateResponse;
import com.edussafy.clone.domain.board.dto.response.BoardCommentResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostCreateResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostDetailResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostLikeResponse;
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
@RequestMapping("/api/v1")
public class BoardController {

    private final BoardQueryService boardQueryService;
    private final BoardCommandService boardCommandService;
    private final BoardCommentService boardCommentService;

    @GetMapping("/boards")
    public ApiResponse<List<BoardResponse>> getBoards() {
        return ApiResponse.ok(boardQueryService.getBoards());
    }

    @GetMapping("/boards/{boardCode}/categories")
    public ApiResponse<List<BoardCategoryResponse>> getCategories(@PathVariable String boardCode) {
        return ApiResponse.ok(boardQueryService.getCategories(boardCode));
    }

    @GetMapping("/boards/{boardCode}/posts")
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

    @GetMapping("/boards/{boardCode}/posts/{postId}")
    public ApiResponse<BoardPostDetailResponse> getPost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser Long currentUserId
    ) {
        return ApiResponse.ok(boardQueryService.getPost(boardCode, postId, currentUserId));
    }

    @PostMapping("/boards/{boardCode}/posts")
    public ApiResponse<BoardPostCreateResponse> createPost(
            @PathVariable String boardCode,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardPostCreateRequest request
    ) {
        Long postId = boardCommandService.createPost(boardCode, currentUser.userId(), currentUser.role(), request.toCommand());
        return ApiResponse.ok(new BoardPostCreateResponse(postId));
    }

    @PatchMapping("/boards/{boardCode}/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardPostUpdateRequest request
    ) {
        boardCommandService.updatePost(boardCode, postId, currentUser.userId(), currentUser.role(), request.toCommand());
        return ApiResponse.ok();
    }

    @DeleteMapping("/boards/{boardCode}/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser CurrentUserPrincipal currentUser
    ) {
        boardCommandService.deletePost(boardCode, postId, currentUser.userId(), currentUser.role());
        return ApiResponse.ok();
    }

    @PostMapping("/boards/{boardCode}/posts/{postId}/like")
    public ApiResponse<BoardPostLikeResponse> likePost(@PathVariable String boardCode, @PathVariable Long postId) {
        int likeCount = boardCommandService.likePost(boardCode, postId);
        return ApiResponse.ok(new BoardPostLikeResponse(postId, likeCount));
    }

    @DeleteMapping("/boards/{boardCode}/posts/{postId}/like")
    public ApiResponse<BoardPostLikeResponse> unlikePost(@PathVariable String boardCode, @PathVariable Long postId) {
        int likeCount = boardCommandService.unlikePost(boardCode, postId);
        return ApiResponse.ok(new BoardPostLikeResponse(postId, likeCount));
    }

    @GetMapping("/boards/{boardCode}/posts/{postId}/comments")
    public ApiResponse<List<BoardCommentResponse>> getComments(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser Long currentUserId
    ) {
        return ApiResponse.ok(boardCommentService.getComments(boardCode, postId, currentUserId));
    }

    @PostMapping("/boards/{boardCode}/posts/{postId}/comments")
    public ApiResponse<BoardCommentCreateResponse> createComment(
            @PathVariable String boardCode,
            @PathVariable Long postId,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardCommentCreateRequest request
    ) {
        return ApiResponse.ok(boardCommentService.createComment(boardCode, postId, currentUser.userId(), request.toCommand()));
    }

    @PatchMapping("/comments/{commentId}")
    public ApiResponse<Void> updateComment(
            @PathVariable Long commentId,
            @CurrentUser CurrentUserPrincipal currentUser,
            @Valid @RequestBody BoardCommentUpdateRequest request
    ) {
        boardCommentService.updateComment(commentId, currentUser.userId(), currentUser.role(), request.toCommand());
        return ApiResponse.ok();
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @CurrentUser CurrentUserPrincipal currentUser
    ) {
        boardCommentService.deleteComment(commentId, currentUser.userId(), currentUser.role());
        return ApiResponse.ok();
    }
}
