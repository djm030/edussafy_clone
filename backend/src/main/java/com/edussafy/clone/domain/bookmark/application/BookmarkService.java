package com.edussafy.clone.domain.bookmark.application;

import com.edussafy.clone.domain.bookmark.domain.entity.UserBookmark;
import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.bookmark.domain.repository.UserBookmarkRepository;
import com.edussafy.clone.domain.bookmark.dto.mapper.BookmarkDtoMapper;
import com.edussafy.clone.domain.bookmark.dto.request.BookmarkRequest;
import com.edussafy.clone.domain.bookmark.dto.response.BookmarkResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {
    private final UserBookmarkRepository userBookmarkRepository;
    private final UserRepository userRepository;
    private final BookmarkDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<BookmarkResponse> getMyBookmarks(Long userId, BookmarkTargetType targetType, int page, int size) {
        User user = getUser(userId);
        return PageResponse.from((targetType == null
                ? userBookmarkRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(page, size))
                : userBookmarkRepository.findByUserAndTargetTypeOrderByCreatedAtDesc(user, targetType, PageRequest.of(page, size)))
                .map(mapper::toResponse));
    }

    public BookmarkResponse addBookmark(Long userId, BookmarkRequest request) {
        User user = getUser(userId);
        return userBookmarkRepository.findByUserAndTargetTypeAndTargetId(user, request.targetType(), request.targetId())
                .map(mapper::toResponse)
                .orElseGet(() -> mapper.toResponse(userBookmarkRepository.save(UserBookmark.builder()
                        .user(user).targetType(request.targetType()).targetId(request.targetId()).build())));
    }

    public void deleteBookmark(Long userId, BookmarkRequest request) {
        User user = getUser(userId);
        userBookmarkRepository.deleteByUserAndTargetTypeAndTargetId(user, request.targetType(), request.targetId());
    }

    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
}
