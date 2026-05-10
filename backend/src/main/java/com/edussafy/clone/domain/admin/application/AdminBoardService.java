package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.enums.BoardType;
import com.edussafy.clone.domain.board.domain.repository.BoardCategoryRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final AdminAccessService adminAccessService;
    private final AuditLogService auditLogService;
    private final BoardRepository boardRepository;
    private final BoardCategoryRepository categoryRepository;
    private final BoardPostRepository postRepository;

    @Transactional
    public Map<String, Object> createBoard(Long adminId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Board board = boardRepository.save(Board.builder()
                .name(str(request, "name"))
                .code(str(request, "code"))
                .boardType(enumValue(BoardType.class, request, "boardType", BoardType.NORMAL))
                .description(str(request, "description"))
                .build());
        auditLogService.record(adminId, AuditAction.CREATE, "BOARD", board.getId(), "게시판 생성");
        return board(board);
    }

    @Transactional
    public Map<String, Object> updateBoard(Long adminId, Long boardId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        board.update(str(request, "name"), str(request, "code"), enumValue(BoardType.class, request, "boardType", board.getBoardType()), str(request, "description"));
        auditLogService.record(adminId, AuditAction.UPDATE, "BOARD", board.getId(), "게시판 수정");
        return board(board);
    }

    @Transactional
    public Map<String, Object> createCategory(Long adminId, Long boardId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        BoardCategory category = categoryRepository.save(BoardCategory.builder().board(board).name(str(request, "name")).code(str(request, "code")).build());
        auditLogService.record(adminId, AuditAction.CREATE, "BOARD_CATEGORY", category.getId(), "게시판 카테고리 생성");
        return category(category);
    }

    @Transactional
    public Map<String, Object> updateCategory(Long adminId, Long categoryId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        BoardCategory category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("board category not found"));
        category.update(str(request, "name"), str(request, "code"));
        auditLogService.record(adminId, AuditAction.UPDATE, "BOARD_CATEGORY", category.getId(), "게시판 카테고리 수정");
        return category(category);
    }

    @Transactional
    public void deleteCategory(Long adminId, Long categoryId) {
        adminAccessService.requireAdmin(adminId);
        BoardCategory category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("board category not found"));
        categoryRepository.delete(category);
        auditLogService.record(adminId, AuditAction.DELETE, "BOARD_CATEGORY", categoryId, "게시판 카테고리 삭제");
    }

    @Transactional
    public void deletePost(Long adminId, Long postId) {
        adminAccessService.requireAdmin(adminId);
        BoardPost post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("board post not found"));
        post.softDelete();
        auditLogService.record(adminId, AuditAction.DELETE, "BOARD_POST", postId, "게시글 관리자 삭제");
    }

    @Transactional
    public void restorePost(Long adminId, Long postId) {
        adminAccessService.requireAdmin(adminId);
        BoardPost post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("board post not found"));
        post.restore();
        auditLogService.record(adminId, AuditAction.UPDATE, "BOARD_POST", postId, "게시글 관리자 복구");
    }

    private static String str(Map<String, Object> request, String key) { Object value = request.get(key); return value == null ? null : value.toString(); }
    private static <E extends Enum<E>> E enumValue(Class<E> type, Map<String, Object> request, String key, E defaultValue) { Object value = request.get(key); return value == null ? defaultValue : Enum.valueOf(type, value.toString()); }
    private static Map<String, Object> board(Board b) { return Map.of("id", b.getId(), "name", b.getName(), "code", b.getCode(), "boardType", b.getBoardType(), "description", b.getDescription()); }
    private static Map<String, Object> category(BoardCategory c) { return Map.of("id", c.getId(), "boardId", c.getBoard().getId(), "name", c.getName(), "code", c.getCode()); }
}
