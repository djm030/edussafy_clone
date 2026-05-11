package com.edussafy.clone.domain.survey.application;

import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.entity.SurveyParticipant;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import com.edussafy.clone.domain.survey.domain.repository.SurveyCategoryRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyParticipantRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyQuestionRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyRepository;
import com.edussafy.clone.domain.survey.dto.mapper.SurveyDtoMapper;
import com.edussafy.clone.domain.survey.dto.request.SurveySubmitRequest;
import com.edussafy.clone.domain.survey.dto.response.SurveyCategoryResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyParticipantResponse;
import com.edussafy.clone.domain.survey.dto.response.SurveyResponse;
import com.edussafy.clone.domain.survey.exception.SurveyNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {
    private final SurveyCategoryRepository surveyCategoryRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyParticipantRepository surveyParticipantRepository;
    private final UserRepository userRepository;
    private final SurveyDtoMapper mapper;
    private final ObjectMapper objectMapper;

    public List<SurveyCategoryResponse> getCategories() { return surveyCategoryRepository.findAll().stream().map(mapper::toCategoryResponse).toList(); }
    public PageResponse<SurveyResponse> getSurveys(Long categoryId, FormType formType, String keyword, int page, int size) {
        return PageResponse.from(surveyRepository.search(categoryId, formType, normalize(keyword), PageRequest.of(page, size))
                .map(s -> mapper.toResponse(s, List.of())));
    }
    public SurveyResponse getSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundException::new);
        return mapper.toResponse(survey, surveyQuestionRepository.findBySurveyIdOrderBySortOrderAscQuestionNoAsc(surveyId));
    }
    @Transactional
    public SurveyParticipantResponse submit(Long surveyId, Long userId, SurveySubmitRequest request) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundException::new);
        User user = getUser(userId);
        SurveyParticipant participant = surveyParticipantRepository.findBySurveyAndUser(survey, user)
                .orElseGet(() -> surveyParticipantRepository.save(SurveyParticipant.builder().survey(survey).user(user).build()));
        participant.submit(toJson(request.answers()));
        return mapper.toParticipantResponse(participant);
    }
    public PageResponse<SurveyParticipantResponse> getMyParticipations(Long userId, FormType formType, int page, int size) {
        User user = getUser(userId);
        if (formType != null) {
            return PageResponse.from(surveyParticipantRepository.findByUserAndSurvey_FormTypeOrderByCreatedAtDesc(user, formType, PageRequest.of(page, size)).map(mapper::toParticipantResponse));
        }

        return PageResponse.from(surveyParticipantRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(page, size)).map(mapper::toParticipantResponse));
    }
    @Transactional
    public SurveyParticipantResponse cancel(Long surveyId, Long userId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(SurveyNotFoundException::new);
        User user = getUser(userId);
        SurveyParticipant participant = surveyParticipantRepository.findBySurveyAndUser(survey, user).orElseThrow(SurveyNotFoundException::new);
        participant.cancel();
        return mapper.toParticipantResponse(participant);
    }
    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
    private String normalize(String keyword) { return keyword == null || keyword.isBlank() ? null : keyword; }
    private String toJson(Object value) { try { return objectMapper.writeValueAsString(value); } catch (JsonProcessingException e) { throw new IllegalArgumentException("Invalid answer data", e); } }
}
