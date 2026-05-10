package com.edussafy.clone.domain.survey.domain.entity;

import com.edussafy.clone.domain.survey.domain.enums.QuestionType;
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
@Table(name = "survey_questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    @Column(nullable = false)
    private Integer questionNo;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private QuestionType questionType;
    @Column(columnDefinition = "json")
    private String options;
    @Column(nullable = false)
    private Boolean isRequired;
    @Column(nullable = false)
    private Integer sortOrder;
    @Builder
    public SurveyQuestion(Long id, Survey survey, Integer questionNo, String questionText, QuestionType questionType, String options, Boolean isRequired, Integer sortOrder) {
        this.id=id; this.survey=survey; this.questionNo=questionNo; this.questionText=questionText; this.questionType=questionType; this.options=options; this.isRequired=isRequired == null ? false : isRequired; this.sortOrder=sortOrder == null ? 0 : sortOrder;
    }

    public void update(Integer questionNo, String questionText, QuestionType questionType, String options, Boolean isRequired, Integer sortOrder) {
        this.questionNo=questionNo; this.questionText=questionText; this.questionType=questionType; this.options=options; this.isRequired=isRequired != null && isRequired; this.sortOrder=sortOrder == null ? this.sortOrder : sortOrder;
    }
}
