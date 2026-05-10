package com.edussafy.clone.domain.board.domain.repository;

import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    List<BoardCategory> findByBoardOrderByIdAsc(Board board);
    Optional<BoardCategory> findByIdAndBoard(Long id, Board board);
}
