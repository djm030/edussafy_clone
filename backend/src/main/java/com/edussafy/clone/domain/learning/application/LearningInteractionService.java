package com.edussafy.clone.domain.learning.application;

import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.entity.UserContentInteraction;
import com.edussafy.clone.domain.learning.domain.enums.ContentInteractionType;
import com.edussafy.clone.domain.learning.domain.repository.LearningContentRepository;
import com.edussafy.clone.domain.learning.domain.repository.UserContentInteractionRepository;
import com.edussafy.clone.domain.learning.dto.response.LearningInteractionResponse;
import com.edussafy.clone.domain.learning.exception.LearningContentNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LearningInteractionService {
    private final LearningContentRepository learningContentRepository;
    private final UserContentInteractionRepository userContentInteractionRepository;
    private final UserRepository userRepository;

    public LearningInteractionResponse like(Long contentId, Long userId) {
        LearningContent content = getContent(contentId);
        User user = getUser(userId);
        content.increaseLikeCount();
        saveInteraction(user, content, ContentInteractionType.LIKE);
        return toInteractionResponse(content);
    }

    public LearningInteractionResponse download(Long contentId, Long userId) {
        LearningContent content = getContent(contentId);
        User user = getUser(userId);
        content.increaseDownloadCount();
        saveInteraction(user, content, ContentInteractionType.DOWNLOAD);
        return toInteractionResponse(content);
    }

    public LearningInteractionResponse record(Long contentId, Long userId, ContentInteractionType interactionType) {
        LearningContent content = getContent(contentId);
        User user = getUser(userId);
        if (interactionType == ContentInteractionType.VIEW || interactionType == ContentInteractionType.PLAY) {
            content.increaseViewCount();
        } else if (interactionType == ContentInteractionType.LIKE) {
            content.increaseLikeCount();
        } else if (interactionType == ContentInteractionType.DOWNLOAD) {
            content.increaseDownloadCount();
        }
        saveInteraction(user, content, interactionType);
        return toInteractionResponse(content);
    }

    private LearningContent getContent(Long contentId) {
        return learningContentRepository.findById(contentId).orElseThrow(LearningContentNotFoundException::new);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void saveInteraction(User user, LearningContent content, ContentInteractionType type) {
        userContentInteractionRepository.save(UserContentInteraction.builder()
                .user(user)
                .content(content)
                .interactionType(type)
                .build());
    }

    private LearningInteractionResponse toInteractionResponse(LearningContent content) {
        return new LearningInteractionResponse(content.getId(), content.getViewCount(), content.getLikeCount(), content.getDownloadCount());
    }
}
