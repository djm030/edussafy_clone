package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminSurveyService;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminSurveyController {
    private final AdminSurveyService service;

    @PostMapping("/survey-categories") public ApiResponse<Map<String,Object>> createCategory(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createCategory(adminId,r));}
    @PatchMapping("/survey-categories/{categoryId}") public ApiResponse<Map<String,Object>> updateCategory(@CurrentUser Long adminId,@PathVariable Long categoryId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateCategory(adminId,categoryId,r));}
    @DeleteMapping("/survey-categories/{categoryId}") public ApiResponse<Void> deleteCategory(@CurrentUser Long adminId,@PathVariable Long categoryId){service.deleteCategory(adminId,categoryId);return ApiResponse.ok();}
    @PostMapping("/surveys") public ApiResponse<Map<String,Object>> createSurvey(@CurrentUser Long adminId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createSurvey(adminId,r));}
    @PatchMapping("/surveys/{surveyId}") public ApiResponse<Map<String,Object>> updateSurvey(@CurrentUser Long adminId,@PathVariable Long surveyId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateSurvey(adminId,surveyId,r));}
    @DeleteMapping("/surveys/{surveyId}") public ApiResponse<Void> deleteSurvey(@CurrentUser Long adminId,@PathVariable Long surveyId){service.deleteSurvey(adminId,surveyId);return ApiResponse.ok();}
    @PostMapping("/surveys/{surveyId}/questions") public ApiResponse<Map<String,Object>> createQuestion(@CurrentUser Long adminId,@PathVariable Long surveyId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.createQuestion(adminId,surveyId,r));}
    @PatchMapping("/survey-questions/{questionId}") public ApiResponse<Map<String,Object>> updateQuestion(@CurrentUser Long adminId,@PathVariable Long questionId,@RequestBody Map<String,Object> r){return ApiResponse.ok(service.updateQuestion(adminId,questionId,r));}
    @DeleteMapping("/survey-questions/{questionId}") public ApiResponse<Void> deleteQuestion(@CurrentUser Long adminId,@PathVariable Long questionId){service.deleteQuestion(adminId,questionId);return ApiResponse.ok();}
    @GetMapping("/surveys/{surveyId}/participants") public ApiResponse<PageResponse<Map<String,Object>>> participants(@CurrentUser Long adminId,@PathVariable Long surveyId,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){return ApiResponse.ok(service.getParticipants(adminId,surveyId,page,size));}
    @PostMapping("/survey-participants/{participantId}/select") public ApiResponse<Map<String,Object>> select(@CurrentUser Long adminId,@PathVariable Long participantId){return ApiResponse.ok(service.selectParticipant(adminId,participantId));}
    @PostMapping("/survey-participants/{participantId}/reject") public ApiResponse<Map<String,Object>> reject(@CurrentUser Long adminId,@PathVariable Long participantId){return ApiResponse.ok(service.rejectParticipant(adminId,participantId));}
}
