package com.edussafy.clone.domain.task.application;

import com.edussafy.clone.domain.task.domain.entity.CourseTask;
import com.edussafy.clone.domain.task.domain.entity.UserTaskResult;
import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import com.edussafy.clone.domain.task.domain.repository.CourseTaskRepository;
import com.edussafy.clone.domain.task.domain.repository.UserTaskResultRepository;
import com.edussafy.clone.domain.task.dto.mapper.TaskDtoMapper;
import com.edussafy.clone.domain.task.dto.request.TaskSubmitRequest;
import com.edussafy.clone.domain.task.dto.response.CourseTaskResponse;
import com.edussafy.clone.domain.task.dto.response.UserTaskResultResponse;
import com.edussafy.clone.domain.task.exception.CourseTaskNotFoundException;
import com.edussafy.clone.domain.task.exception.UserTaskResultNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final CourseTaskRepository courseTaskRepository;
    private final UserTaskResultRepository userTaskResultRepository;
    private final UserRepository userRepository;
    private final TaskDtoMapper mapper;
    private final ObjectMapper objectMapper;

    public PageResponse<CourseTaskResponse> getMyTasks(Long userId, Long courseId, CourseTaskType taskType, TaskResultStatus resultStatus, int page, int size) {
        User user = getUser(userId);
        if (resultStatus != null) {
            return PageResponse.from(userTaskResultRepository.findByUserAndResultStatusOrderByUpdatedAtDesc(user, resultStatus, PageRequest.of(page, size))
                    .map(r -> mapper.toTaskResponse(r.getTask(), r.getResultStatus())));
        }
        return PageResponse.from(courseTaskRepository.search(courseId, taskType, PageRequest.of(page, size))
                .map(t -> mapper.toTaskResponse(t, userTaskResultRepository.findByTaskAndUser(t, user).map(UserTaskResult::getResultStatus).orElse(null))));
    }
    public CourseTaskResponse getTask(Long taskId, Long userId) {
        CourseTask task = courseTaskRepository.findById(taskId).orElseThrow(CourseTaskNotFoundException::new);
        User user = getUser(userId);
        return mapper.toTaskResponse(task, userTaskResultRepository.findByTaskAndUser(task, user).map(UserTaskResult::getResultStatus).orElse(null));
    }
    @Transactional
    public UserTaskResultResponse submit(Long taskId, Long userId, TaskSubmitRequest request) {
        CourseTask task = courseTaskRepository.findById(taskId).orElseThrow(CourseTaskNotFoundException::new);
        User user = getUser(userId);
        UserTaskResult result = userTaskResultRepository.findByTaskAndUser(task, user)
                .orElseGet(() -> userTaskResultRepository.save(UserTaskResult.builder().task(task).user(user).build()));
        result.submit(toJson(request.answerData()));
        return mapper.toResultResponse(result);
    }
    public UserTaskResultResponse getMyResult(Long taskId, Long userId) {
        CourseTask task = courseTaskRepository.findById(taskId).orElseThrow(CourseTaskNotFoundException::new);
        User user = getUser(userId);
        return mapper.toResultResponse(userTaskResultRepository.findByTaskAndUser(task, user).orElseThrow(UserTaskResultNotFoundException::new));
    }
    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
    private String toJson(Object value) { try { return objectMapper.writeValueAsString(value); } catch (JsonProcessingException e) { throw new IllegalArgumentException("Invalid answer data", e); } }
}
