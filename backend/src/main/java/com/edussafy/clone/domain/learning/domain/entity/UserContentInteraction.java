package com.edussafy.clone.domain.learning.domain.entity;

import com.edussafy.clone.domain.learning.domain.enums.ContentInteractionType;
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
@Table(name = "user_content_interactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserContentInteraction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "content_id", nullable = false)
    private LearningContent content;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ContentInteractionType interactionType;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public UserContentInteraction(Long id, User user, LearningContent content, ContentInteractionType interactionType) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.interactionType = interactionType;
        this.createdAt = LocalDateTime.now();
    }
}
