package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.admin.dto.mapper.AdminDtoMapper;
import com.edussafy.clone.domain.admin.dto.request.AdminPointAdjustRequest;
import com.edussafy.clone.domain.point.domain.entity.PointTransaction;
import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.point.domain.repository.PointTransactionRepository;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
import com.edussafy.clone.domain.point.dto.mapper.PointDtoMapper;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPointService {
    private final UserRepository userRepository;
    private final UserStatRepository userStatRepository;
    private final PointTransactionRepository transactionRepository;
    private final PointDtoMapper pointMapper;
    private final AdminDtoMapper adminMapper;
    private final AuditLogService auditLogService;
    private final AdminAccessService adminAccessService;

    @Transactional(readOnly = true)
    public PointSummaryResponse getUserPoint(Long adminId, Long userId) {
        adminAccessService.requireAdmin(adminId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return pointMapper.toSummaryResponse(userStatRepository.findByUser(user).orElse(null));
    }

    @Transactional
    public PointTransactionResponse adjust(Long adminId, AdminPointAdjustRequest request) {
        adminAccessService.requireAdmin(adminId);
        User user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);
        User admin = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);
        PointTransaction tx = transactionRepository.save(PointTransaction.builder().user(user).createdBy(admin)
                .transactionType(request.transactionType() == null ? PointTransactionType.ADJUST : request.transactionType())
                .pointAmount(request.pointAmount()).expAmount(request.expAmount()).reason(request.reason()).targetType("ADMIN_ADJUST").build());
        UserStat stat = userStatRepository.findByUser(user).orElseGet(() -> userStatRepository.save(UserStat.builder().user(user).scholarshipPoint(0).totalExp(0).levelName("Lv.1").levelNo(1).attendanceRate(0.0).completedLearningCount(0).build()));
        stat.adjust(tx.getPointAmount(), tx.getExpAmount());
        auditLogService.record(adminId, AuditAction.ADJUST, "POINT_TRANSACTION", tx.getId(), "포인트/경험치 수동 조정");
        return adminMapper.toPointTransactionResponse(tx);
    }

    @Transactional(readOnly = true)
    public PageResponse<PointTransactionResponse> getTransactions(Long adminId, Long userId, PointTransactionType type, LocalDate startDate, LocalDate endDate, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<PointTransaction> spec = Specification.where(null);
        if (userId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("user").get("id"), userId));
        if (type != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("transactionType"), type));
        if (startDate != null) spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
        if (endDate != null) spec = spec.and((root, q, cb) -> cb.lessThan(root.get("createdAt"), endDate.plusDays(1).atStartOfDay()));
        return PageResponse.from(transactionRepository.findAll(spec, PageRequest.of(page, size)).map(adminMapper::toPointTransactionResponse));
    }
}
