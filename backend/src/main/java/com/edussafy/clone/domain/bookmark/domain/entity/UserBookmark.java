package com.edussafy.clone.domain.bookmark.domain.entity;

import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_bookmarks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBookmark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private BookmarkTargetType targetType;
    @Column(nullable = false)
    private Long targetId;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public UserBookmark(Long id, User user, BookmarkTargetType targetType, Long targetId) {
        this.id = id; this.user = user; this.targetType = targetType; this.targetId = targetId; this.createdAt = LocalDateTime.now();
    }
}
