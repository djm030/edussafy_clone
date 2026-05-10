package com.edussafy.clone.domain.inquiry.domain.repository;

import com.edussafy.clone.domain.inquiry.domain.entity.Inquiry;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {
    Page<Inquiry> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user, Pageable pageable);
    Page<Inquiry> findByUserAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(User user, InquiryStatus status, Pageable pageable);
}
