package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.admin.dto.mapper.AdminDtoMapper;
import com.edussafy.clone.domain.admin.dto.request.AdminPasswordResetRequest;
import com.edussafy.clone.domain.admin.dto.request.AdminUserCreateRequest;
import com.edussafy.clone.domain.admin.dto.request.AdminUserUpdateRequest;
import com.edussafy.clone.domain.admin.dto.response.AdminUserImportResponse;
import com.edussafy.clone.domain.admin.dto.response.AdminUserResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final UserStatRepository userStatRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final AdminAccessService adminAccessService;
    private final AdminDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<AdminUserResponse> getUsers(Long adminId, String keyword, UserRole role, UserStatus status, Integer generation, String region, Integer classNo, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<User> spec = Specification.where(null);
        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword + "%";
            spec = spec.and((root, q, cb) -> cb.or(cb.like(root.get("email"), like), cb.like(root.get("name"), like), cb.like(root.get("studentNo"), like)));
        }
        if (role != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("role"), role));
        if (status != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        if (generation != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("generation"), generation));
        if (region != null && !region.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("region"), region));
        if (classNo != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("classNo"), classNo));
        return PageResponse.from(userRepository.findAll(spec, PageRequest.of(page, size)).map(mapper::toUserResponse));
    }

    @Transactional(readOnly = true)
    public AdminUserResponse getUser(Long adminId, Long userId) { adminAccessService.requireAdmin(adminId); return mapper.toUserResponse(findUser(userId)); }

    @Transactional
    public AdminUserResponse createUser(Long adminId, AdminUserCreateRequest request) {
        adminAccessService.requireAdmin(adminId);
        User user = User.builder().email(request.email()).password(passwordEncoder.encode(request.initialPassword())).name(request.name())
                .studentNo(request.studentNo()).generation(request.generation()).region(request.region()).classNo(request.classNo())
                .phoneNumber(request.phoneNumber()).emergencyPhoneNumber(request.emergencyPhoneNumber()).zipCode(request.zipCode())
                .address(request.address()).addressDetail(request.addressDetail()).role(request.role() == null ? UserRole.STUDENT : request.role())
                .status(UserStatus.ACTIVE).build();
        User saved = userRepository.save(user);
        userStatRepository.save(UserStat.builder().user(saved).scholarshipPoint(0).totalExp(0).levelName("Lv.1").levelNo(1).attendanceRate(0.0).completedLearningCount(0).updatedAt(LocalDateTime.now()).build());
        auditLogService.record(adminId, AuditAction.CREATE, "USER", saved.getId(), "회원 단건 등록");
        return mapper.toUserResponse(saved);
    }

    @Transactional
    public AdminUserImportResponse importUsers(Long adminId, MultipartFile file, String defaultPassword, UserRole role) {
        adminAccessService.requireAdmin(adminId);
        List<AdminUserImportResponse.FailedRow> failed = new ArrayList<>();
        int total = 0; int success = 0;
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            String[] lines = content.split("\r?\n");
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) continue; total++;
                String[] c = lines[i].split(",", -1);
                try {
                    if (c.length < 2) throw new IllegalArgumentException("email/name 컬럼이 부족합니다.");
                    String email = c[0].trim(); String name = c[1].trim();
                    if (userRepository.findByEmail(email).isPresent()) throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                    User user = User.builder().email(email).name(name).password(passwordEncoder.encode(c.length > 12 && !c[12].isBlank() ? c[12].trim() : defaultPassword))
                            .studentNo(c.length > 2 ? blankToNull(c[2]) : null).generation(c.length > 3 ? parseInt(c[3]) : null)
                            .region(c.length > 4 ? blankToNull(c[4]) : null).classNo(c.length > 5 ? parseInt(c[5]) : null)
                            .phoneNumber(c.length > 6 ? blankToNull(c[6]) : null).emergencyPhoneNumber(c.length > 7 ? blankToNull(c[7]) : null)
                            .zipCode(c.length > 8 ? blankToNull(c[8]) : null).address(c.length > 9 ? blankToNull(c[9]) : null).addressDetail(c.length > 10 ? blankToNull(c[10]) : null)
                            .role(c.length > 11 && !c[11].isBlank() ? UserRole.valueOf(c[11].trim()) : role).status(UserStatus.ACTIVE).build();
                    User saved = userRepository.save(user);
                    userStatRepository.save(UserStat.builder().user(saved).scholarshipPoint(0).totalExp(0).levelName("Lv.1").levelNo(1).attendanceRate(0.0).completedLearningCount(0).updatedAt(LocalDateTime.now()).build());
                    success++;
                } catch (Exception e) { failed.add(new AdminUserImportResponse.FailedRow(i + 1, c.length > 0 ? c[0].trim() : null, e.getMessage())); }
            }
        } catch (Exception e) { failed.add(new AdminUserImportResponse.FailedRow(0, null, "파일을 읽을 수 없습니다.")); }
        auditLogService.record(adminId, AuditAction.CREATE, "USER_IMPORT", null, "회원 일괄 등록");
        return new AdminUserImportResponse(total, success, failed.size(), failed);
    }

    @Transactional
    public AdminUserResponse updateUser(Long adminId, Long userId, AdminUserUpdateRequest request) {
        adminAccessService.requireAdmin(adminId);
        User user = findUser(userId);
        user.updateByAdmin(request.name(), request.generation(), request.region(), request.classNo(), request.phoneNumber(), request.emergencyPhoneNumber(), request.zipCode(), request.address(), request.addressDetail(), request.role(), request.status());
        auditLogService.record(adminId, AuditAction.UPDATE, "USER", userId, "회원 정보 수정");
        return mapper.toUserResponse(user);
    }

    @Transactional
    public void resetPassword(Long adminId, Long userId, AdminPasswordResetRequest request) {
        adminAccessService.requireAdmin(adminId);
        User user = findUser(userId);
        user.changePassword(passwordEncoder.encode(request.newPassword()));
        auditLogService.record(adminId, AuditAction.UPDATE, "USER_PASSWORD", userId, "회원 비밀번호 초기화");
    }

    private User findUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
    private static String blankToNull(String v) { return v == null || v.isBlank() ? null : v.trim(); }
    private static Integer parseInt(String v) { return v == null || v.isBlank() ? null : Integer.parseInt(v.trim()); }
}
