package com.edussafy.clone.domain.board.domain.entity;

import com.edussafy.clone.domain.board.domain.enums.ContentType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, length = 30)
    private ContentType contentType;

    @Column(name = "content_text", columnDefinition = "LONGTEXT")
    private String contentText;

    @Column(name = "content_html", columnDefinition = "LONGTEXT")
    private String contentHtml;

    @Column(name = "content_json", columnDefinition = "json")
    private String contentJson;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Integer commentCount;

    @Column(nullable = false)
    private Integer scrapCount;

    @Column(nullable = false)
    private Boolean hasAttachment;

    @Column(nullable = false)
    private Boolean isNotice;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public BoardPost(Long id, BoardCategory category, User user, String title, ContentType contentType,
                     String contentText, String contentHtml, String contentJson, Integer viewCount,
                     Integer likeCount, Integer commentCount, Integer scrapCount, Boolean hasAttachment,
                     Boolean isNotice, Boolean isDeleted) {
        this.id = id;
        this.category = category;
        this.user = user;
        this.title = title;
        this.contentType = contentType == null ? ContentType.HTML : contentType;
        this.contentText = contentText;
        this.contentHtml = contentHtml;
        this.contentJson = contentJson;
        this.viewCount = viewCount == null ? 0 : viewCount;
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.commentCount = commentCount == null ? 0 : commentCount;
        this.scrapCount = scrapCount == null ? 0 : scrapCount;
        this.hasAttachment = hasAttachment != null && hasAttachment;
        this.isNotice = isNotice != null && isNotice;
        this.isDeleted = isDeleted != null && isDeleted;
    }

    public Board getBoard() {
        return category.getBoard();
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void update(BoardCategory category, String title, ContentType contentType,
                       String contentText, String contentHtml, String contentJson, boolean hasAttachment) {
        this.category = category;
        this.title = title;
        this.contentType = contentType;
        this.contentText = contentText;
        this.contentHtml = contentHtml;
        this.contentJson = contentJson;
        this.hasAttachment = hasAttachment;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }

    public void increaseCommentCount() {
        this.commentCount += 1;
    }

    public void decreaseCommentCount() {
        this.commentCount = Math.max(0, this.commentCount - 1);
    }

    public void updateAttachmentState(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public boolean isWrittenBy(Long userId) {
        return user != null && user.getId() != null && user.getId().equals(userId);
    }
}
