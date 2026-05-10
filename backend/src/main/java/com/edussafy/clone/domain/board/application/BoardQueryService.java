package com.edussafy.clone.domain.board.application;

import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.repository.BoardCategoryRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardRepository;
import com.edussafy.clone.domain.board.dto.mapper.BoardDtoMapper;
import com.edussafy.clone.domain.board.dto.response.BoardCategoryResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostDetailResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostListResponse;
import com.edussafy.clone.domain.board.dto.response.BoardResponse;
import com.edussafy.clone.domain.board.exception.BoardCategoryNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardNotFoundException;
import com.edussafy.clone.domain.board.exception.BoardPostNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardPostRepository boardPostRepository;
    private final BoardDtoMapper boardDtoMapper;

    public List<BoardResponse> getBoards() {
        return boardRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(boardDtoMapper::toBoardResponse)
                .toList();
    }

    public List<BoardCategoryResponse> getCategories(String boardCode) {
        Board board = getBoard(boardCode);
        return boardCategoryRepository.findByBoardOrderByIdAsc(board).stream()
                .map(boardDtoMapper::toCategoryResponse)
                .toList();
    }

    public PageResponse<BoardPostListResponse> getPosts(String boardCode, Long categoryId, String keyword,
                                                        int page, int size, Long currentUserId) {
        Board board = getBoard(boardCode);
        BoardCategory category = resolveCategory(board, categoryId);
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100),
                Sort.by(Sort.Order.desc("isNotice"), Sort.Order.desc("createdAt")));
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        Page<BoardPostListResponse> posts = boardPostRepository.search(board, category, normalizedKeyword, pageable)
                .map(post -> boardDtoMapper.toListResponse(post, currentUserId));
        return PageResponse.from(posts);
    }

    @Transactional
    public BoardPostDetailResponse getPost(String boardCode, Long postId, Long currentUserId) {
        Board board = getBoard(boardCode);
        BoardPost post = boardPostRepository.findReadablePost(board, postId).orElseThrow(BoardPostNotFoundException::new);
        post.increaseViewCount();
        return boardDtoMapper.toDetailResponse(post, currentUserId);
    }

    private Board getBoard(String boardCode) {
        return boardRepository.findByCodeIgnoreCase(boardCode).orElseThrow(BoardNotFoundException::new);
    }

    private BoardCategory resolveCategory(Board board, Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return boardCategoryRepository.findByIdAndBoard(categoryId, board).orElseThrow(BoardCategoryNotFoundException::new);
    }
}
