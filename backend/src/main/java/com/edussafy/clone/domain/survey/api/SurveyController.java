package com.edussafy.clone.domain.survey.api;

import com.edussafy.clone.domain.survey.application.SurveyService;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import com.edussafy.clone.domain.survey.dto.request.SurveySubmitRequest;
import com.edussafy.clone.domain.survey.dto.response.SurveyCategoryResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyParticipantResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/v1")
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/survey-categories")
    public ApiResponse<List<SurveyCategoryResponse>> getCategories() { return ApiResponse.ok(surveyService.getCategories()); }

    @GetMapping("/surveys")
    public ApiResponse<PageResponse<SurveyResponse>> getSurveys(@RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) FormType formType, @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(surveyService.getSurveys(categoryId, formType, keyword, page, size));
    }

    @GetMapping("/surveys/{surveyId}")
    public ApiResponse<SurveyResponse> getSurvey(@PathVariable Long surveyId) { return ApiResponse.ok(surveyService.getSurvey(surveyId)); }

    @PostMapping("/surveys/{surveyId}/submit")
    public ApiResponse<SurveyParticipantResponse> submit(@PathVariable Long surveyId, @CurrentUser Long currentUserId,
            @Valid @RequestBody SurveySubmitRequest request) {
        return ApiResponse.ok(surveyService.submit(surveyId, currentUserId, request));
    }

    @GetMapping("/surveys/my-participations")
    public ApiResponse<PageResponse<SurveyParticipantResponse>> getMyParticipations(@CurrentUser Long currentUserId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(surveyService.getMyParticipations(currentUserId, page, size));
    }

    @PostMapping("/surveys/{surveyId}/cancel")
    public ApiResponse<SurveyParticipantResponse> cancel(@PathVariable Long surveyId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(surveyService.cancel(surveyId, currentUserId));
    }
}
