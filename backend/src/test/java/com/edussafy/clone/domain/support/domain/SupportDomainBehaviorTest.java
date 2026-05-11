package com.edussafy.clone.domain.support.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.entity.UserAgreement;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementType;
import com.edussafy.clone.domain.inquiry.domain.entity.Inquiry;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.notification.domain.entity.Notification;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.survey.domain.entity.SurveyParticipant;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import com.edussafy.clone.domain.survey.domain.enums.ParticipantStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import org.junit.jupiter.api.Test;

class SupportDomainBehaviorTest {

    @Test
    void surveyParticipant_submit_cancel_select_and_reject_update_statuses() {
        SurveyParticipant participant = SurveyParticipant.builder()
                .survey(survey())
                .user(user(1L, UserRole.STUDENT))
                .build();

        assertThat(participant.getParticipantStatus()).isEqualTo(ParticipantStatus.TARGETED);

        participant.submit("{\"choice\":1}");
        assertThat(participant.getParticipantStatus()).isEqualTo(ParticipantStatus.SUBMITTED);
        assertThat(participant.getSubmittedAt()).isNotNull();

        participant.cancel();
        assertThat(participant.getParticipantStatus()).isEqualTo(ParticipantStatus.CANCELLED);
        assertThat(participant.getCancelledAt()).isNotNull();

        participant.select();
        assertThat(participant.getParticipantStatus()).isEqualTo(ParticipantStatus.SELECTED);

        participant.reject();
        assertThat(participant.getParticipantStatus()).isEqualTo(ParticipantStatus.REJECTED);
    }

    @Test
    void inquiry_answer_close_delete_and_notification_read_behaviors_are_stateful() {
        User student = user(1L, UserRole.STUDENT);
        User operator = user(2L, UserRole.OPERATOR);
        Inquiry inquiry = Inquiry.builder()
                .user(student)
                .category("general")
                .title("question")
                .content("body")
                .build();
        Notification notification = Notification.builder()
                .receiver(student)
                .title("notice")
                .content("body")
                .notificationType(NotificationType.SYSTEM)
                .build();

        inquiry.answer("answer", operator);
        notification.markRead();

        assertThat(inquiry.getStatus()).isEqualTo(InquiryStatus.ANSWERED);
        assertThat(inquiry.getAnsweredBy()).isSameAs(operator);
        assertThat(notification.getIsRead()).isTrue();
        assertThat(notification.getReadAt()).isNotNull();

        inquiry.close();
        inquiry.delete();

        assertThat(inquiry.getStatus()).isEqualTo(InquiryStatus.CLOSED);
        assertThat(inquiry.getIsDeleted()).isTrue();
    }

    @Test
    void agreement_defaults_active_and_userAgreement_records_current_version() {
        Agreement agreement = Agreement.builder()
                .category(AgreementCategory.PRIVACY)
                .title("Privacy")
                .agreementType(AgreementType.PRIVACY)
                .version("v1")
                .build();
        UserAgreement userAgreement = UserAgreement.builder()
                .agreement(agreement)
                .user(user(1L, UserRole.STUDENT))
                .agreedVersion(agreement.getVersion())
                .ipAddress("127.0.0.1")
                .userAgent("test")
                .build();

        assertThat(agreement.getIsActive()).isTrue();
        assertThat(agreement.getIsRequired()).isFalse();
        assertThat(userAgreement.getAgreedVersion()).isEqualTo("v1");
        assertThat(userAgreement.getAgreedAt()).isNotNull();

        agreement.inactive();

        assertThat(agreement.getIsActive()).isFalse();
    }

    private Survey survey() {
        return Survey.builder()
                .id(1L)
                .title("survey")
                .formType(FormType.SURVEY)
                .build();
    }

    private User user(Long id, UserRole role) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .password("encoded")
                .name("User " + id)
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
