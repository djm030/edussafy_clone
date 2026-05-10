package com.edussafy.clone.domain.bookmark.exception;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class BookmarkNotFoundException extends BusinessException { public BookmarkNotFoundException() { super(ErrorCode.BOOKMARK_NOT_FOUND); } }
