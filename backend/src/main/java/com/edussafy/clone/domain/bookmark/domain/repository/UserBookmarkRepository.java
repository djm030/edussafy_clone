package com.edussafy.clone.domain.bookmark.domain.repository;

import com.edussafy.clone.domain.bookmark.domain.entity.UserBookmark;
import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookmarkRepository extends JpaRepository<UserBookmark, Long> {
    Page<UserBookmark> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Page<UserBookmark> findByUserAndTargetTypeOrderByCreatedAtDesc(User user, BookmarkTargetType targetType, Pageable pageable);
    Optional<UserBookmark> findByUserAndTargetTypeAndTargetId(User user, BookmarkTargetType targetType, Long targetId);
    boolean existsByUserAndTargetTypeAndTargetId(User user, BookmarkTargetType targetType, Long targetId);
    void deleteByUserAndTargetTypeAndTargetId(User user, BookmarkTargetType targetType, Long targetId);
}
