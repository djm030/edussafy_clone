package com.edussafy.clone.domain.survey.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "survey_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Builder
    public SurveyCategory(Long id, String name, String code, String description) { this.id=id; this.name=name; this.code=code; this.description=description; }

    public void update(String name, String code, String description) { this.name=name; this.code=code; this.description=description; }
}
