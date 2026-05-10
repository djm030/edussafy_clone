package com.edussafy.clone.domain.task.api;

import com.edussafy.clone.domain.task.application.TaskService;
import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import com.edussafy.clone.domain.task.dto.request.TaskSubmitRequest;
import com.edussafy.clone.domain.task.dto.response.CourseTaskResponse;
import com.edussafy.clone.domain.task.dto.response.UserTaskResultResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<CourseTaskResponse>> getMyTasks(@CurrentUser Long currentUserId,
            @RequestParam(required = false) Long courseId, @RequestParam(required = false) CourseTaskType taskType,
            @RequestParam(required = false) TaskResultStatus resultStatus, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(taskService.getMyTasks(currentUserId, courseId, taskType, resultStatus, page, size));
    }

    @GetMapping("/{taskId}")
    public ApiResponse<CourseTaskResponse> getTask(@PathVariable Long taskId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(taskService.getTask(taskId, currentUserId));
    }

    @PostMapping("/{taskId}/submit")
    public ApiResponse<UserTaskResultResponse> submit(@PathVariable Long taskId, @CurrentUser Long currentUserId,
            @Valid @RequestBody TaskSubmitRequest request) {
        return ApiResponse.ok(taskService.submit(taskId, currentUserId, request));
    }

    @GetMapping("/{taskId}/my-result")
    public ApiResponse<UserTaskResultResponse> getMyResult(@PathVariable Long taskId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(taskService.getMyResult(taskId, currentUserId));
    }
}
