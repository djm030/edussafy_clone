package com.edussafy.clone.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "게시판을 찾을 수 없습니다."),
    BOARD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_CATEGORY_NOT_FOUND", "게시판 카테고리를 찾을 수 없습니다."),
    BOARD_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    BOARD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND", "파일을 찾을 수 없습니다."),
    FILE_STORAGE_FAILED(HttpStatus.BAD_REQUEST, "FILE_STORAGE_FAILED", "파일 처리에 실패했습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "과정을 찾을 수 없습니다."),
    COURSE_WEEK_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_WEEK_NOT_FOUND", "과정 주차를 찾을 수 없습니다."),
    COURSE_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_SESSION_NOT_FOUND", "강의 세션을 찾을 수 없습니다."),
    LEARNING_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "LEARNING_CONTENT_NOT_FOUND", "학습 콘텐츠를 찾을 수 없습니다."),
    LEARNING_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "LEARNING_CATEGORY_NOT_FOUND", "학습 카테고리를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
