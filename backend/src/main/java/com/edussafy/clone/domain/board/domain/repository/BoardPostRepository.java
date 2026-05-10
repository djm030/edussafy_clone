package com.edussafy.clone.domain.board.domain.repository;

import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    @Query("""
            select p from BoardPost p
            join fetch p.category c
            join fetch c.board b
            left join fetch p.user u
            where b = :board
              and p.isDeleted = false
              and (:category is null or c = :category)
              and (:keyword is null or lower(p.title) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(p.contentText, '')) like lower(concat('%', :keyword, '%')))
            """)
    Page<BoardPost> search(@Param("board") Board board,
                           @Param("category") BoardCategory category,
                           @Param("keyword") String keyword,
                           Pageable pageable);

    @Query("""
            select p from BoardPost p
            join fetch p.category c
            join fetch c.board b
            left join fetch p.user u
            where p.id = :postId and b = :board and p.isDeleted = false
            """)
    Optional<BoardPost> findReadablePost(@Param("board") Board board, @Param("postId") Long postId);
}
