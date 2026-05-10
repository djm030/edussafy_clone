package com.edussafy.clone.domain.board.domain.repository;

import com.edussafy.clone.domain.board.domain.entity.BoardComment;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByPostAndIsDeletedFalseOrderByCreatedAtAscIdAsc(BoardPost post);
}
