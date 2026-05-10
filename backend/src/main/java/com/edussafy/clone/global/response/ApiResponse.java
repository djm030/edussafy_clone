package com.edussafy.clone.global.response;

public record ApiResponse<T>(
        boolean success,
        T data,
        String errorCode,
        String message
) {
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청이 성공했습니다.";

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, DEFAULT_SUCCESS_MESSAGE);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, null, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> ApiResponse<T> fail(String errorCode, String message) {
        return new ApiResponse<>(false, null, errorCode, message);
    }
}
