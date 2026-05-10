package com.edussafy.clone.domain.inquiry.domain.entity;

import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String category;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private InquiryStatus status;
    @Column(columnDefinition = "TEXT")
    private String answerContent;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "answered_by_id")
    private User answeredBy;
    private LocalDateTime answeredAt;
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Inquiry(Long id, User user, String category, String title, String content, InquiryStatus status, String answerContent, User answeredBy, LocalDateTime answeredAt, Boolean isDeleted) {
        this.id=id; this.user=user; this.category=category; this.title=title; this.content=content; this.status=status == null ? InquiryStatus.WAITING : status; this.answerContent=answerContent; this.answeredBy=answeredBy; this.answeredAt=answeredAt; this.isDeleted=isDeleted == null ? false : isDeleted;
    }
    public void update(String category, String title, String content) { this.category=category; this.title=title; this.content=content; }
    public void answer(String answerContent, User answeredBy) { this.answerContent=answerContent; this.answeredBy=answeredBy; this.answeredAt=LocalDateTime.now(); this.status=InquiryStatus.ANSWERED; }
    public void close() { this.status=InquiryStatus.CLOSED; }
    public void delete() { this.isDeleted = true; }
}
