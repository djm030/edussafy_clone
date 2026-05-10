package com.edussafy.clone.domain.inquiry.application;

import com.edussafy.clone.domain.inquiry.domain.entity.Inquiry;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.inquiry.domain.repository.InquiryRepository;
import com.edussafy.clone.domain.inquiry.dto.mapper.InquiryDtoMapper;
import com.edussafy.clone.domain.inquiry.dto.request.InquiryCreateRequest;
import com.edussafy.clone.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.edussafy.clone.domain.inquiry.dto.response.InquiryResponse;
import com.edussafy.clone.domain.inquiry.exception.InquiryNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.file.FileRole;
import com.edussafy.clone.global.file.FileTargetType;
import com.edussafy.clone.global.response.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final FileResourceRepository fileResourceRepository;
    private final InquiryDtoMapper mapper;

    public PageResponse<InquiryResponse> getMyInquiries(Long userId, InquiryStatus status, int page, int size) {
        User user = getUser(userId);
        return PageResponse.from((status == null
                ? inquiryRepository.findByUserAndIsDeletedFalseOrderByCreatedAtDesc(user, PageRequest.of(page, size))
                : inquiryRepository.findByUserAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(user, status, PageRequest.of(page, size)))
                .map(mapper::toResponse));
    }
    public InquiryResponse getInquiry(Long inquiryId, Long currentUserId) {
        Inquiry inquiry = getVisibleInquiry(inquiryId);
        assertOwnerOrAdmin(inquiry, getUser(currentUserId));
        return mapper.toResponse(inquiry);
    }
    @Transactional
    public InquiryResponse create(Long userId, InquiryCreateRequest request) {
        User user = getUser(userId);
        Inquiry inquiry = inquiryRepository.save(Inquiry.builder().user(user).category(request.category()).title(request.title()).content(request.content()).build());
        linkFiles(request.fileIds(), inquiry.getId());
        return mapper.toResponse(inquiry);
    }
    @Transactional
    public InquiryResponse update(Long inquiryId, Long userId, InquiryUpdateRequest request) {
        Inquiry inquiry = getVisibleInquiry(inquiryId);
        User user = getUser(userId);
        assertOwnerOrAdmin(inquiry, user);
        if (inquiry.getStatus() != InquiryStatus.WAITING) throw new BusinessException(ErrorCode.INVALID_REQUEST);
        inquiry.update(request.category(), request.title(), request.content());
        return mapper.toResponse(inquiry);
    }
    @Transactional
    public void delete(Long inquiryId, Long userId) {
        Inquiry inquiry = getVisibleInquiry(inquiryId);
        assertOwnerOrAdmin(inquiry, getUser(userId));
        inquiry.delete();
    }
    private Inquiry getVisibleInquiry(Long id) { return inquiryRepository.findById(id).filter(i -> !Boolean.TRUE.equals(i.getIsDeleted())).orElseThrow(InquiryNotFoundException::new); }
    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
    private void assertOwnerOrAdmin(Inquiry inquiry, User user) {
        if (inquiry.getUser().getId().equals(user.getId()) || user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.OPERATOR) return;
        throw new BusinessException(ErrorCode.ACCESS_DENIED);
    }
    private void linkFiles(List<Long> fileIds, Long inquiryId) {
        if (fileIds == null || fileIds.isEmpty()) return;
        List<FileResource> files = fileResourceRepository.findByIdIn(fileIds);
        for (int i = 0; i < files.size(); i++) files.get(i).linkTo(FileTargetType.INQUIRY, inquiryId, FileRole.ATTACHMENT, i);
    }
}
