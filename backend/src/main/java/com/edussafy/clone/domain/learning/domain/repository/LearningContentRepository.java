package com.edussafy.clone.domain.learning.domain.repository;

import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearningContentRepository extends JpaRepository<LearningContent, Long> {
    @Query("""
            select c from LearningContent c
            where (:categoryId is null or c.category.id = :categoryId)
              and (:courseId is null or c.course.id = :courseId)
              and (:contentType is null or c.contentType = :contentType)
              and (:requiredOnly = false or c.isRequired = true)
              and (:keyword is null or lower(c.title) like lower(concat('%', :keyword, '%'))
                   or lower(c.description) like lower(concat('%', :keyword, '%')))
            order by c.openAt desc nulls last, c.id desc
            """)
    Page<LearningContent> search(@Param("categoryId") Long categoryId,
                                 @Param("courseId") Long courseId,
                                 @Param("contentType") LearningContentType contentType,
                                 @Param("keyword") String keyword,
                                 @Param("requiredOnly") boolean requiredOnly,
                                 Pageable pageable);
}
