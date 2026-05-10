package com.edussafy.clone.domain.learning.application;

import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.entity.UserLearningProgress;
import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import com.edussafy.clone.domain.learning.domain.repository.LearningContentRepository;
import com.edussafy.clone.domain.learning.domain.repository.UserLearningProgressRepository;
import com.edussafy.clone.domain.learning.dto.request.LearningProgressRequest;
import com.edussafy.clone.domain.learning.dto.response.LearningProgressResponse;
import com.edussafy.clone.domain.learning.exception.LearningContentNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LearningProgressService {
    private final LearningContentRepository learningContentRepository;
    private final UserLearningProgressRepository userLearningProgressRepository;
    private final UserRepository userRepository;

    public LearningProgressResponse saveProgress(Long contentId, Long userId, LearningProgressRequest request) {
        LearningContent content = getContent(contentId);
        User user = getUser(userId);
        UserLearningProgress progress = userLearningProgressRepository.findByUserAndContent(user, content)
                .orElseGet(() -> UserLearningProgress.builder().user(user).content(content).build());
        progress.saveProgress(request.progressRate(), request.lastPositionSeconds(), request.progressStatus());
        return toResponse(userLearningProgressRepository.save(progress));
    }

    public LearningProgressResponse complete(Long contentId, Long userId) {
        LearningContent content = getContent(contentId);
        User user = getUser(userId);
        UserLearningProgress progress = userLearningProgressRepository.findByUserAndContent(user, content)
                .orElseGet(() -> UserLearningProgress.builder().user(user).content(content).build());
        progress.complete();
        return toResponse(userLearningProgressRepository.save(progress));
    }

    @Transactional(readOnly = true)
    public PageResponse<LearningProgressResponse> getMyProgress(Long userId, LearningProgressStatus status, int page, int size) {
        User user = getUser(userId);
        Page<UserLearningProgress> progresses = status == null
                ? userLearningProgressRepository.findByUser(user, PageRequest.of(page, size))
                : userLearningProgressRepository.findByUserAndProgressStatus(user, status, PageRequest.of(page, size));
        return PageResponse.from(progresses.map(this::toResponse));
    }

    private LearningContent getContent(Long contentId) {
        return learningContentRepository.findById(contentId).orElseThrow(LearningContentNotFoundException::new);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private LearningProgressResponse toResponse(UserLearningProgress progress) {
        return new LearningProgressResponse(progress.getId(), progress.getContent().getId(), progress.getContent().getTitle(),
                progress.getProgressStatus(), progress.getProgressRate(), progress.getLastPositionSeconds(),
                progress.getStartedAt(), progress.getCompletedAt(), progress.getLastAccessedAt());
    }
}
