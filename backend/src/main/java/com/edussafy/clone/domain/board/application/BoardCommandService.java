package com.edussafy.clone.domain.board.application;

import com.edussafy.clone.domain.board.application.command.CreateBoardPostCommand;
import com.edussafy.clone.domain.board.application.command.UpdateBoardPostCommand;
import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.repository.BoardCategoryRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardRepository;
import com.edussafy.clone.domain.board.exception.BoardAccessDeniedException;
import com.edussafy.clone.domain.board.exception.BoardCategoryNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardPostNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;
    private final BoardAttachmentService boardAttachmentService;

    public Long createPost(String boardCode, Long currentUserId, UserRole currentUserRole, CreateBoardPostCommand command) {
        Board board = getBoard(boardCode);
        validateWritable(board, currentUserRole);
        BoardCategory category = getCategory(board, command.categoryId());
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        BoardPost post = BoardPost.builder()
                .category(category)
                .user(user)
                .title(command.title())
                .contentType(command.contentType())
                .contentText(command.contentText())
                .contentHtml(command.contentHtml())
                .contentJson(command.contentJson())
                .hasAttachment(command.fileIds() != null && !command.fileIds().isEmpty())
                .isNotice(board.isAdminWritable())
                .build();
        BoardPost savedPost = boardPostRepository.save(post);
        boardAttachmentService.linkPostFiles(savedPost.getId(), command.fileIds());
        return savedPost.getId();
    }

    public void updatePost(String boardCode, Long postId, Long currentUserId, UserRole currentUserRole, UpdateBoardPostCommand command) {
        Board board = getBoard(boardCode);
        BoardPost post = boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
        validateOwnerOrAdmin(post, currentUserId, currentUserRole);
        BoardCategory category = getCategory(board, command.categoryId());
        post.update(category, command.title(), command.contentType(), command.contentText(), command.contentHtml(),
                command.contentJson(), command.fileIds() != null && !command.fileIds().isEmpty());
        boardAttachmentService.linkPostFiles(post.getId(), command.fileIds());
    }

    public void deletePost(String boardCode, Long postId, Long currentUserId, UserRole currentUserRole) {
        Board board = getBoard(boardCode);
        BoardPost post = boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
        validateOwnerOrAdmin(post, currentUserId, currentUserRole);
        post.softDelete();
    }

    public int likePost(String boardCode, Long postId) {
        Board board = getBoard(boardCode);
        BoardPost post = boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
        post.increaseLikeCount();
        return post.getLikeCount();
    }

    public int unlikePost(String boardCode, Long postId) {
        Board board = getBoard(boardCode);
        BoardPost post = boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
        post.decreaseLikeCount();
        return post.getLikeCount();
    }

    private Board getBoard(String boardCode) {
        return boardRepository.findByCodeIgnoreCase(boardCode).orElseThrow(BoardNotFoundException::new);
    }

    private BoardCategory getCategory(Board board, Long categoryId) {
        return boardCategoryRepository.findByIdAndBoard(categoryId, board).orElseThrow(BoardCategoryNotFoundException::new);
    }

    private void validateWritable(Board board, UserRole role) {
        if (board.isAdminWritable() && !isAdmin(role)) {
            throw new BoardAccessDeniedException();
        }
    }

    private void validateOwnerOrAdmin(BoardPost post, Long currentUserId, UserRole role) {
        if (!post.isWrittenBy(currentUserId) && !isAdmin(role)) {
            throw new BoardAccessDeniedException();
        }
    }

    private boolean isAdmin(UserRole role) {
        return role == UserRole.ADMIN || role == UserRole.OPERATOR;
    }
}
