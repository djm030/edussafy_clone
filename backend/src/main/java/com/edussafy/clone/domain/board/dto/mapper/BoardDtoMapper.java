package com.edussafy.clone.domain.board.dto.mapper;

import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.dto.response.BoardCategoryResponse;
import com.edussafy.clone.domain.board.dto.response.BoardFileResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostDetailResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostListResponse;
import com.edussafy.clone.domain.board.dto.response.BoardResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BoardDtoMapper {

    private static final String ANONYMOUS_DISPLAY_NAME = "익명";
    private static final String DELETED_USER_DISPLAY_NAME = "탈퇴회원";

    public BoardResponse toBoardResponse(Board board) {
        return new BoardResponse(board.getId(), board.getName(), board.getCode(), board.getBoardType(), board.getDescription());
    }

    public BoardCategoryResponse toCategoryResponse(BoardCategory category) {
        return new BoardCategoryResponse(category.getId(), category.getName(), category.getCode());
    }

    public BoardPostListResponse toListResponse(BoardPost post, Long currentUserId) {
        return new BoardPostListResponse(
                post.getId(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getTitle(),
                displayName(post),
                post.isWrittenBy(currentUserId),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getHasAttachment(),
                post.getIsNotice(),
                post.getCreatedAt()
        );
    }

    public BoardPostDetailResponse toDetailResponse(BoardPost post, Long currentUserId, List<BoardFileResponse> files) {
        return new BoardPostDetailResponse(
                post.getId(),
                post.getCategory().getId(),
                post.getCategory().getName(),
                post.getTitle(),
                post.getContentType(),
                post.getContentText(),
                post.getContentHtml(),
                post.getContentJson(),
                displayName(post),
                post.isWrittenBy(currentUserId),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getScrapCount(),
                post.getHasAttachment(),
                post.getIsNotice(),
                files,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private String displayName(BoardPost post) {
        if (post.getBoard().isAnonymous()) {
            return ANONYMOUS_DISPLAY_NAME;
        }
        return post.getUser() == null ? DELETED_USER_DISPLAY_NAME : post.getUser().getName();
    }
}
