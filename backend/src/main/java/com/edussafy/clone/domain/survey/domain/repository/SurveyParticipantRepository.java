package com.edussafy.clone.domain.survey.domain.repository;

import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.entity.SurveyParticipant;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SurveyParticipantRepository extends JpaRepository<SurveyParticipant, Long>, JpaSpecificationExecutor<SurveyParticipant> {
    Optional<SurveyParticipant> findBySurveyAndUser(Survey survey, User user);
    Page<SurveyParticipant> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
