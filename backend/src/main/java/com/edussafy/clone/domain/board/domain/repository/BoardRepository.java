package com.edussafy.clone.domain.board.domain.repository;

import com.edussafy.clone.domain.board.domain.entity.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByCodeIgnoreCase(String code);
}
