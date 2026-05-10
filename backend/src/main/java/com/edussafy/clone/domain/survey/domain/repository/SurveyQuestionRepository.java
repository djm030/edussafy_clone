package com.edussafy.clone.domain.survey.domain.repository;

import com.edussafy.clone.domain.survey.domain.entity.SurveyQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findBySurveyIdOrderBySortOrderAscQuestionNoAsc(Long surveyId);
}
