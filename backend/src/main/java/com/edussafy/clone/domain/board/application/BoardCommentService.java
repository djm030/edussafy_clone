package com.edussafy.clone.domain.board.application;

import com.edussafy.clone.domain.board.application.command.CreateBoardCommentCommand;
import com.edussafy.clone.domain.board.application.command.UpdateBoardCommentCommand;
import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardComment;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.repository.BoardCommentRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardRepository;
import com.edussafy.clone.domain.board.dto.response.BoardCommentCreateResponse;
import com.edussafy.clone.domain.board.dto.response.BoardCommentResponse;
import com.edussafy.clone.domain.board.exception.BoardAccessDeniedException;
import com.edussafy.clone.domain.board.exception.BoardCommentNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardPostNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommentService {

    private static final String ANONYMOUS_DISPLAY_NAME = "익명";
    private static final String DELETED_USER_DISPLAY_NAME = "탈퇴회원";

    private final BoardRepository boardRepository;
    private final BoardPostRepository boardPostRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BoardCommentResponse> getComments(String boardCode, Long postId, Long currentUserId) {
        BoardPost post = getReadablePost(boardCode, postId);
        return boardCommentRepository.findByPostAndIsDeletedFalseOrderByCreatedAtAscIdAsc(post).stream()
                .map(comment -> toResponse(comment, currentUserId))
                .toList();
    }

    public BoardCommentCreateResponse createComment(String boardCode, Long postId, Long currentUserId,
                                                    CreateBoardCommentCommand command) {
        BoardPost post = getReadablePost(boardCode, postId);
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        BoardComment parent = command.parentId() == null ? null : getComment(command.parentId());
        BoardComment comment = BoardComment.builder()
                .post(post)
                .user(user)
                .parent(parent)
                .content(command.content())
                .build();
        BoardComment savedComment = boardCommentRepository.save(comment);
        post.increaseCommentCount();
        return new BoardCommentCreateResponse(savedComment.getId());
    }

    public void updateComment(Long commentId, Long currentUserId, UserRole role, UpdateBoardCommentCommand command) {
        BoardComment comment = getComment(commentId);
        validateOwnerOrAdmin(comment, currentUserId, role);
        comment.update(command.content());
    }

    public void deleteComment(Long commentId, Long currentUserId, UserRole role) {
        BoardComment comment = getComment(commentId);
        validateOwnerOrAdmin(comment, currentUserId, role);
        if (!comment.getIsDeleted()) {
            comment.softDelete();
            comment.getPost().decreaseCommentCount();
        }
    }

    private BoardPost getReadablePost(String boardCode, Long postId) {
        Board board = boardRepository.findByCodeIgnoreCase(boardCode).orElseThrow(BoardNotFoundException::new);
        return boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
    }

    private BoardComment getComment(Long commentId) {
        return boardCommentRepository.findById(commentId)
                .filter(comment -> !comment.getIsDeleted())
                .orElseThrow(BoardCommentNotFoundException::new);
    }

    private BoardCommentResponse toResponse(BoardComment comment, Long currentUserId) {
        return new BoardCommentResponse(
                comment.getId(),
                comment.getParent() == null ? null : comment.getParent().getId(),
                comment.getContent(),
                displayName(comment),
                comment.isWrittenBy(currentUserId),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    private String displayName(BoardComment comment) {
        if (comment.getPost().getBoard().isAnonymous()) {
            return ANONYMOUS_DISPLAY_NAME;
        }
        return comment.getUser() == null ? DELETED_USER_DISPLAY_NAME : comment.getUser().getName();
    }

    private void validateOwnerOrAdmin(BoardComment comment, Long currentUserId, UserRole role) {
        if (!comment.isWrittenBy(currentUserId) && !isAdmin(role)) {
            throw new BoardAccessDeniedException();
        }
    }

    private boolean isAdmin(UserRole role) {
        return role == UserRole.ADMIN || role == UserRole.OPERATOR;
    }
}
