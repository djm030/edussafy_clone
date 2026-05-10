package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.entity.UserAgreement;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementType;
import com.edussafy.clone.domain.agreement.domain.repository.AgreementRepository;
import com.edussafy.clone.domain.agreement.domain.repository.UserAgreementRepository;
import com.edussafy.clone.domain.agreement.dto.mapper.AgreementDtoMapper;
import com.edussafy.clone.domain.agreement.dto.response.AgreementResponse;
import com.edussafy.clone.domain.agreement.dto.response.UserAgreementResponse;
import com.edussafy.clone.domain.inquiry.domain.entity.Inquiry;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.inquiry.domain.repository.InquiryRepository;
import com.edussafy.clone.domain.inquiry.dto.mapper.InquiryDtoMapper;
import com.edussafy.clone.domain.inquiry.dto.response.InquiryResponse;
import com.edussafy.clone.domain.notification.domain.entity.Notification;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.notification.domain.repository.NotificationRepository;
import com.edussafy.clone.domain.notification.dto.mapper.NotificationDtoMapper;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.response.PageResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSupportService {
    private final AdminAccessService adminAccessService;
    private final AuditLogService auditLogService;
    private final InquiryRepository inquiryRepository;
    private final NotificationRepository notificationRepository;
    private final AgreementRepository agreementRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final UserRepository userRepository;
    private final FileResourceRepository fileRepository;
    private final InquiryDtoMapper inquiryMapper;
    private final NotificationDtoMapper notificationMapper;
    private final AgreementDtoMapper agreementMapper;

    @Transactional(readOnly = true)
    public PageResponse<InquiryResponse> getInquiries(Long adminId, InquiryStatus status, Long userId, String category, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<Inquiry> spec = Specification.where((root, q, cb) -> cb.isFalse(root.get("isDeleted")));
        if (status != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        if (userId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("user").get("id"), userId));
        if (category != null && !category.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("category"), category));
        return PageResponse.from(inquiryRepository.findAll(spec, PageRequest.of(page, size)).map(inquiryMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public InquiryResponse getInquiry(Long adminId, Long inquiryId) {
        adminAccessService.requireAdmin(adminId);
        return inquiryMapper.toResponse(inquiry(inquiryId));
    }

    @Transactional
    public InquiryResponse answerInquiry(Long adminId, Long inquiryId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Inquiry inquiry = inquiry(inquiryId);
        inquiry.answer(str(request, "answerContent"), user(adminId));
        auditLogService.record(adminId, AuditAction.ANSWER, "INQUIRY", inquiryId, "1:1 문의 답변 등록/수정");
        return inquiryMapper.toResponse(inquiry);
    }

    @Transactional
    public InquiryResponse closeInquiry(Long adminId, Long inquiryId) {
        adminAccessService.requireAdmin(adminId);
        Inquiry inquiry = inquiry(inquiryId);
        inquiry.close();
        auditLogService.record(adminId, AuditAction.UPDATE, "INQUIRY", inquiryId, "1:1 문의 종료");
        return inquiryMapper.toResponse(inquiry);
    }

    @Transactional
    public NotificationResponse sendNotification(Long adminId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Notification saved = notificationRepository.save(Notification.builder()
                .sender(user(adminId))
                .receiver(user(longVal(request, "receiverId")))
                .title(str(request, "title"))
                .content(str(request, "content"))
                .notificationType(en(NotificationType.class, request, "notificationType", NotificationType.SYSTEM))
                .targetType(str(request, "targetType"))
                .targetId(longVal(request, "targetId"))
                .isImportant(bool(request, "isImportant"))
                .metadata(str(request, "metadata"))
                .build());
        auditLogService.record(adminId, AuditAction.SEND, "NOTIFICATION", saved.getId(), "알림 발송");
        return notificationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getNotifications(Long adminId, Long receiverId, NotificationType type, Boolean isRead, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<Notification> spec = Specification.where(null);
        if (receiverId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("receiver").get("id"), receiverId));
        if (type != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("notificationType"), type));
        if (isRead != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("isRead"), isRead));
        return PageResponse.from(notificationRepository.findAll(spec, PageRequest.of(page, size)).map(notificationMapper::toResponse));
    }

    @Transactional
    public AgreementResponse createAgreement(Long adminId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Agreement agreement = agreementRepository.save(Agreement.builder()
                .category(en(AgreementCategory.class, request, "category", AgreementCategory.ETC))
                .targetType(en(AgreementTargetType.class, request, "targetType", AgreementTargetType.GLOBAL))
                .targetId(longVal(request, "targetId"))
                .title(str(request, "title"))
                .contentHtml(str(request, "contentHtml"))
                .attachmentFile(optFile(longVal(request, "attachmentFileId")))
                .agreementType(en(AgreementType.class, request, "agreementType", AgreementType.ETC))
                .version(str(request, "version"))
                .isRequired(bool(request, "isRequired"))
                .isActive(bool(request, "isActive"))
                .sortOrder(integer(request, "sortOrder"))
                .build());
        auditLogService.record(adminId, AuditAction.CREATE, "AGREEMENT", agreement.getId(), "약관 생성");
        return agreementMapper.toResponse(agreement);
    }

    @Transactional
    public AgreementResponse updateAgreement(Long adminId, Long agreementId, Map<String, Object> request) {
        adminAccessService.requireAdmin(adminId);
        Agreement agreement = agreement(agreementId);
        agreement.update(en(AgreementCategory.class, request, "category", agreement.getCategory()), en(AgreementTargetType.class, request, "targetType", agreement.getTargetType()), longVal(request, "targetId"), str(request, "title"), str(request, "contentHtml"), optFile(longVal(request, "attachmentFileId")), en(AgreementType.class, request, "agreementType", agreement.getAgreementType()), str(request, "version"), bool(request, "isRequired"), bool(request, "isActive"), integer(request, "sortOrder"));
        auditLogService.record(adminId, AuditAction.UPDATE, "AGREEMENT", agreementId, "약관 수정");
        return agreementMapper.toResponse(agreement);
    }

    @Transactional
    public void inactiveAgreement(Long adminId, Long agreementId) {
        adminAccessService.requireAdmin(adminId);
        agreement(agreementId).inactive();
        auditLogService.record(adminId, AuditAction.UPDATE, "AGREEMENT", agreementId, "약관 비활성화");
    }

    @Transactional(readOnly = true)
    public PageResponse<UserAgreementResponse> getUserAgreements(Long adminId, Long userId, Long agreementId, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<UserAgreement> spec = Specification.where(null);
        if (userId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("user").get("id"), userId));
        if (agreementId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("agreement").get("id"), agreementId));
        return PageResponse.from(userAgreementRepository.findAll(spec, PageRequest.of(page, size)).map(agreementMapper::toUserResponse));
    }

    private Inquiry inquiry(Long id) { return inquiryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("inquiry not found")); }
    private Agreement agreement(Long id) { return agreementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("agreement not found")); }
    private User user(Long id) { return userRepository.findById(id).orElseThrow(UserNotFoundException::new); }
    private FileResource optFile(Long id) { return id == null ? null : fileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("file not found")); }
    private static String str(Map<String, Object> r, String k) { Object v = r.get(k); return v == null ? null : v.toString(); }
    private static Long longVal(Map<String, Object> r, String k) { Object v = r.get(k); return v == null || v.toString().isBlank() ? null : Long.valueOf(v.toString()); }
    private static Integer integer(Map<String, Object> r, String k) { Object v = r.get(k); return v == null || v.toString().isBlank() ? null : Integer.valueOf(v.toString()); }
    private static Boolean bool(Map<String, Object> r, String k) { Object v = r.get(k); return v == null ? null : Boolean.valueOf(v.toString()); }
    private static <E extends Enum<E>> E en(Class<E> type, Map<String, Object> r, String k, E d) { String v = str(r, k); return v == null || v.isBlank() ? d : Enum.valueOf(type, v); }
}
