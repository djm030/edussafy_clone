package com.edussafy.clone.domain.learning.domain.entity;

import com.edussafy.clone.domain.learning.domain.enums.LearningCategoryType;
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
@Table(name = "learning_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningCategory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_id")
    private LearningCategory parent;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private LearningCategoryType categoryType;

    @Builder
    public LearningCategory(Long id, LearningCategory parent, String name, String code, LearningCategoryType categoryType) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.code = code;
        this.categoryType = categoryType;
    }

    public void update(LearningCategory parent, String name, String code, LearningCategoryType categoryType) {
        this.parent = parent;
        this.name = name;
        this.code = code;
        this.categoryType = categoryType;
    }
}
