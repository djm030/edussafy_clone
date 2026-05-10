package com.edussafy.clone.domain.bookmark.dto.mapper;

import com.edussafy.clone.domain.bookmark.domain.entity.UserBookmark;
import com.edussafy.clone.domain.bookmark.dto.response.BookmarkResponse;
import org.springframework.stereotype.Component;

@Component
public class BookmarkDtoMapper {
    public BookmarkResponse toResponse(UserBookmark bookmark) {
        return new BookmarkResponse(bookmark.getId(), bookmark.getTargetType(), bookmark.getTargetId(), bookmark.getCreatedAt());
    }
}
