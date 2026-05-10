package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminLearningService;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminLearningController {
    private final AdminLearningService service;

    @PostMapping("/courses") public ApiResponse<Map<String,Object>> createCourse(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createCourse(adminId,r));}
    @PatchMapping("/courses/{courseId}") public ApiResponse<Map<String,Object>> updateCourse(@CurrentUser Long adminId,@PathVariable Long courseId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateCourse(adminId,courseId,r));}
    @DeleteMapping("/courses/{courseId}") public ApiResponse<Void> deleteCourse(@CurrentUser Long adminId,@PathVariable Long courseId){service.deleteCourse(adminId,courseId);return ApiResponse.ok();}

    @PostMapping("/learning/categories") public ApiResponse<Map<String,Object>> createLearningCategory(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createCategory(adminId,r));}
    @PatchMapping("/learning/categories/{categoryId}") public ApiResponse<Map<String,Object>> updateLearningCategory(@CurrentUser Long adminId,@PathVariable Long categoryId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateCategory(adminId,categoryId,r));}
    @DeleteMapping("/learning/categories/{categoryId}") public ApiResponse<Void> deleteLearningCategory(@CurrentUser Long adminId,@PathVariable Long categoryId){service.deleteCategory(adminId,categoryId);return ApiResponse.ok();}

    @PostMapping("/courses/{courseId}/weeks") public ApiResponse<Map<String,Object>> createWeek(@CurrentUser Long adminId,@PathVariable Long courseId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createWeek(adminId,courseId,r));}
    @PatchMapping("/course-weeks/{weekId}") public ApiResponse<Map<String,Object>> updateWeek(@CurrentUser Long adminId,@PathVariable Long weekId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateWeek(adminId,weekId,r));}
    @DeleteMapping("/course-weeks/{weekId}") public ApiResponse<Void> deleteWeek(@CurrentUser Long adminId,@PathVariable Long weekId){service.deleteWeek(adminId,weekId);return ApiResponse.ok();}

    @PostMapping("/course-sessions") public ApiResponse<Map<String,Object>> createSession(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createSession(adminId,r));}
    @PatchMapping("/course-sessions/{sessionId}") public ApiResponse<Map<String,Object>> updateSession(@CurrentUser Long adminId,@PathVariable Long sessionId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateSession(adminId,sessionId,r));}
    @DeleteMapping("/course-sessions/{sessionId}") public ApiResponse<Void> deleteSession(@CurrentUser Long adminId,@PathVariable Long sessionId){service.deleteSession(adminId,sessionId);return ApiResponse.ok();}

    @PostMapping("/learning/contents") public ApiResponse<Map<String,Object>> createContent(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createContent(adminId,r));}
    @PatchMapping("/learning/contents/{contentId}") public ApiResponse<Map<String,Object>> updateContent(@CurrentUser Long adminId,@PathVariable Long contentId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateContent(adminId,contentId,r));}
    @DeleteMapping("/learning/contents/{contentId}") public ApiResponse<Void> deleteContent(@CurrentUser Long adminId,@PathVariable Long contentId){service.deleteContent(adminId,contentId);return ApiResponse.ok();}

    @PostMapping("/tasks") public ApiResponse<Map<String,Object>> createTask(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createTask(adminId,r));}
    @PatchMapping("/tasks/{taskId}") public ApiResponse<Map<String,Object>> updateTask(@CurrentUser Long adminId,@PathVariable Long taskId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateTask(adminId,taskId,r));}
    @PatchMapping("/task-results/{resultId}") public ApiResponse<Map<String,Object>> updateTaskResult(@CurrentUser Long adminId,@PathVariable Long resultId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateTaskResult(adminId,resultId,r));}
    @DeleteMapping("/tasks/{taskId}") public ApiResponse<Void> deleteTask(@CurrentUser Long adminId,@PathVariable Long taskId){service.deleteTask(adminId,taskId);return ApiResponse.ok();}

    @PostMapping("/activities") public ApiResponse<Map<String,Object>> createActivity(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createActivity(adminId,r));}
    @PatchMapping("/activities/{activityId}") public ApiResponse<Map<String,Object>> updateActivity(@CurrentUser Long adminId,@PathVariable Long activityId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateActivity(adminId,activityId,r));}
    @DeleteMapping("/activities/{activityId}") public ApiResponse<Void> deleteActivity(@CurrentUser Long adminId,@PathVariable Long activityId){service.deleteActivity(adminId,activityId);return ApiResponse.ok();}
}
