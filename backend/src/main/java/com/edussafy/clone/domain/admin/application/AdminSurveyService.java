package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.entity.SurveyCategory;
import com.edussafy.clone.domain.survey.domain.entity.SurveyParticipant;
import com.edussafy.clone.domain.survey.domain.entity.SurveyQuestion;
import com.edussafy.clone.domain.survey.domain.enums.EventType;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import com.edussafy.clone.domain.survey.domain.enums.QuestionType;
import com.edussafy.clone.domain.survey.domain.repository.SurveyCategoryRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyParticipantRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyQuestionRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyRepository;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSurveyService {
    private final AdminAccessService adminAccessService;
    private final AuditLogService auditLogService;
    private final SurveyCategoryRepository categoryRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository questionRepository;
    private final SurveyParticipantRepository participantRepository;
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    @Transactional public Map<String,Object> createCategory(Long adminId, Map<String,Object> r){adminAccessService.requireAdmin(adminId); SurveyCategory e=categoryRepository.save(SurveyCategory.builder().name(str(r,"name")).code(str(r,"code")).description(str(r,"description")).build()); auditLogService.record(adminId,AuditAction.CREATE,"SURVEY_CATEGORY",e.getId(),"설문 카테고리 생성"); return category(e);}    
    @Transactional public Map<String,Object> updateCategory(Long adminId, Long id, Map<String,Object> r){adminAccessService.requireAdmin(adminId); SurveyCategory e=category(id); e.update(str(r,"name"),str(r,"code"),str(r,"description")); auditLogService.record(adminId,AuditAction.UPDATE,"SURVEY_CATEGORY",id,"설문 카테고리 수정"); return category(e);}    
    @Transactional public void deleteCategory(Long adminId, Long id){adminAccessService.requireAdmin(adminId); categoryRepository.delete(category(id)); auditLogService.record(adminId,AuditAction.DELETE,"SURVEY_CATEGORY",id,"설문 카테고리 삭제");}

    @Transactional public Map<String,Object> createSurvey(Long adminId, Map<String,Object> r){adminAccessService.requireAdmin(adminId); User admin=user(adminId); Survey e=surveyRepository.save(Survey.builder().category(optCategory(longVal(r,"categoryId"))).title(str(r,"title")).description(str(r,"description")).formType(en(FormType.class,r,"formType",FormType.SURVEY)).openAt(dateTime(r,"openAt")).closeAt(dateTime(r,"closeAt")).isRequired(bool(r,"isRequired")).eventType(en(EventType.class,r,"eventType",null)).location(str(r,"location")).capacity(integer(r,"capacity")).selectionPolicy(str(r,"selectionPolicy")).linkedPost(optPost(longVal(r,"linkedPostId"))).createdBy(admin).build()); auditLogService.record(adminId,AuditAction.CREATE,"SURVEY",e.getId(),"설문 생성"); return survey(e);}    
    @Transactional public Map<String,Object> updateSurvey(Long adminId, Long id, Map<String,Object> r){adminAccessService.requireAdmin(adminId); Survey e=survey(id); e.update(optCategory(longVal(r,"categoryId")),str(r,"title"),str(r,"description"),en(FormType.class,r,"formType",e.getFormType()),dateTime(r,"openAt"),dateTime(r,"closeAt"),bool(r,"isRequired"),en(EventType.class,r,"eventType",null),str(r,"location"),integer(r,"capacity"),str(r,"selectionPolicy"),optPost(longVal(r,"linkedPostId"))); auditLogService.record(adminId,AuditAction.UPDATE,"SURVEY",id,"설문 수정"); return survey(e);}    
    @Transactional public void deleteSurvey(Long adminId, Long id){adminAccessService.requireAdmin(adminId); surveyRepository.delete(survey(id)); auditLogService.record(adminId,AuditAction.DELETE,"SURVEY",id,"설문 삭제");}

    @Transactional public Map<String,Object> createQuestion(Long adminId, Long surveyId, Map<String,Object> r){adminAccessService.requireAdmin(adminId); SurveyQuestion e=questionRepository.save(SurveyQuestion.builder().survey(survey(surveyId)).questionNo(integer(r,"questionNo")).questionText(str(r,"questionText")).questionType(en(QuestionType.class,r,"questionType",QuestionType.TEXT)).options(str(r,"options")).isRequired(bool(r,"isRequired")).sortOrder(integer(r,"sortOrder")).build()); auditLogService.record(adminId,AuditAction.CREATE,"SURVEY_QUESTION",e.getId(),"설문 문항 생성"); return question(e);}    
    @Transactional public Map<String,Object> updateQuestion(Long adminId, Long id, Map<String,Object> r){adminAccessService.requireAdmin(adminId); SurveyQuestion e=question(id); e.update(integer(r,"questionNo"),str(r,"questionText"),en(QuestionType.class,r,"questionType",e.getQuestionType()),str(r,"options"),bool(r,"isRequired"),integer(r,"sortOrder")); auditLogService.record(adminId,AuditAction.UPDATE,"SURVEY_QUESTION",id,"설문 문항 수정"); return question(e);}    
    @Transactional public void deleteQuestion(Long adminId, Long id){adminAccessService.requireAdmin(adminId); questionRepository.delete(question(id)); auditLogService.record(adminId,AuditAction.DELETE,"SURVEY_QUESTION",id,"설문 문항 삭제");}

    @Transactional(readOnly = true) public PageResponse<Map<String,Object>> getParticipants(Long adminId, Long surveyId, int page, int size){adminAccessService.requireAdmin(adminId); survey(surveyId); Specification<SurveyParticipant> spec=(root,q,cb)->cb.equal(root.get("survey").get("id"),surveyId); return PageResponse.from(participantRepository.findAll(spec, PageRequest.of(page,size)).map(this::participant));}
    @Transactional public Map<String,Object> selectParticipant(Long adminId, Long id){adminAccessService.requireAdmin(adminId); SurveyParticipant e=participant(id); e.select(); auditLogService.record(adminId,AuditAction.APPROVE,"SURVEY_PARTICIPANT",id,"신청자 선정"); return participant(e);}    
    @Transactional public Map<String,Object> rejectParticipant(Long adminId, Long id){adminAccessService.requireAdmin(adminId); SurveyParticipant e=participant(id); e.reject(); auditLogService.record(adminId,AuditAction.REJECT,"SURVEY_PARTICIPANT",id,"신청자 탈락"); return participant(e);}    

    private SurveyCategory category(Long id){return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("survey category not found"));} private SurveyCategory optCategory(Long id){return id==null?null:category(id);} private Survey survey(Long id){return surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("survey not found"));} private SurveyQuestion question(Long id){return questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("survey question not found"));} private SurveyParticipant participant(Long id){return participantRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("survey participant not found"));} private BoardPost optPost(Long id){return id==null?null:boardPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));} private User user(Long id){return userRepository.findById(id).orElseThrow(UserNotFoundException::new);}    
    private static String str(Map<String,Object> r,String k){Object v=r.get(k);return v==null?null:v.toString();} private static Long longVal(Map<String,Object> r,String k){Object v=r.get(k);return v==null||v.toString().isBlank()?null:Long.valueOf(v.toString());} private static Integer integer(Map<String,Object> r,String k){Object v=r.get(k);return v==null||v.toString().isBlank()?null:Integer.valueOf(v.toString());} private static Boolean bool(Map<String,Object> r,String k){Object v=r.get(k);return v==null?null:Boolean.valueOf(v.toString());} private static LocalDateTime dateTime(Map<String,Object> r,String k){String v=str(r,k);return v==null||v.isBlank()?null:LocalDateTime.parse(v);} private static <E extends Enum<E>> E en(Class<E> t,Map<String,Object> r,String k,E d){String v=str(r,k);return v==null||v.isBlank()?d:Enum.valueOf(t,v);} private static Map<String,Object> m(){return new LinkedHashMap<>();} private static void p(Map<String,Object> m,String k,Object v){m.put(k,v);}    
    private static Map<String,Object> category(SurveyCategory e){Map<String,Object> m=m();p(m,"id",e.getId());p(m,"name",e.getName());p(m,"code",e.getCode());p(m,"description",e.getDescription());return m;} private static Map<String,Object> survey(Survey e){Map<String,Object> m=m();p(m,"id",e.getId());p(m,"categoryId",e.getCategory()==null?null:e.getCategory().getId());p(m,"title",e.getTitle());p(m,"formType",e.getFormType());p(m,"capacity",e.getCapacity());return m;} private static Map<String,Object> question(SurveyQuestion e){Map<String,Object> m=m();p(m,"id",e.getId());p(m,"surveyId",e.getSurvey().getId());p(m,"questionNo",e.getQuestionNo());p(m,"questionText",e.getQuestionText());p(m,"questionType",e.getQuestionType());return m;} private Map<String,Object> participant(SurveyParticipant e){Map<String,Object> m=m();p(m,"id",e.getId());p(m,"surveyId",e.getSurvey().getId());p(m,"userId",e.getUser().getId());p(m,"participantStatus",e.getParticipantStatus());p(m,"submittedAt",e.getSubmittedAt());return m;}
}
