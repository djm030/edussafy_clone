package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.entity.AuditLog;
import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.admin.domain.repository.AuditLogRepository;
import com.edussafy.clone.domain.admin.dto.mapper.AdminDtoMapper;
import com.edussafy.clone.domain.admin.dto.response.AuditLogResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AdminAccessService adminAccessService;
    private final AdminDtoMapper mapper;

    @Transactional
    public void record(Long actorId, AuditAction action, String targetType, Long targetId, String message) {
        User actor = actorId == null ? null : userRepository.findById(actorId).orElseThrow(UserNotFoundException::new);
        auditLogRepository.save(AuditLog.builder().actor(actor).action(action).targetType(targetType).targetId(targetId).message(message).build());
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> getAuditLogs(Long adminId, Long actorId, AuditAction action, String targetType, Long targetId, LocalDate startDate, LocalDate endDate, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<AuditLog> spec = Specification.where(null);
        if (actorId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("actor").get("id"), actorId));
        if (action != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("action"), action));
        if (targetType != null && !targetType.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("targetType"), targetType));
        if (targetId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("targetId"), targetId));
        if (startDate != null) spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate.atStartOfDay()));
        if (endDate != null) spec = spec.and((root, q, cb) -> cb.lessThan(root.get("createdAt"), endDate.plusDays(1).atStartOfDay()));
        return PageResponse.from(auditLogRepository.findAll(spec, PageRequest.of(page, size)).map(mapper::toAuditLogResponse));
    }

    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLog(Long adminId, Long auditLogId) {
        adminAccessService.requireAdmin(adminId);
        return mapper.toAuditLogResponse(auditLogRepository.findById(auditLogId).orElseThrow(() -> new IllegalArgumentException("audit log not found")));
    }
}
