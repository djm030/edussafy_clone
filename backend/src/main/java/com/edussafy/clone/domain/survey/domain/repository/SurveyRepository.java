package com.edussafy.clone.domain.survey.domain.repository;

import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @Query("""
            select s from Survey s
            where (:categoryId is null or s.category.id = :categoryId)
              and (:formType is null or s.formType = :formType)
              and (:keyword is null or lower(s.title) like lower(concat('%', :keyword, '%')))
            order by s.openAt desc, s.id desc
            """)
    Page<Survey> search(@Param("categoryId") Long categoryId, @Param("formType") FormType formType, @Param("keyword") String keyword, Pageable pageable);
}
