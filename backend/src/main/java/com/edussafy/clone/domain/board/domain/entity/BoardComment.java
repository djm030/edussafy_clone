package com.edussafy.clone.domain.board.domain.entity;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private BoardPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public BoardComment(Long id, BoardPost post, User user, BoardComment parent, String content, Boolean isDeleted) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.content = content;
        this.isDeleted = isDeleted != null && isDeleted;
    }

    public void update(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public boolean isWrittenBy(Long userId) {
        return user != null && user.getId() != null && user.getId().equals(userId);
    }
}
