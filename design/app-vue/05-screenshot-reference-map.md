# Screenshot reference map

이 문서는 route/page별로 반드시 확인해야 할 screenshot을 연결한다.
`FRAME.md`와 `DESIGN.md`의 내용을 반복하지 않고, **어떤 이미지를 열어 봐야 하는지**만 지정한다.

## Agent instruction

페이지 구현 시 파일명만 보고 추측하지 말고, 반드시 명시된 screenshot 이미지를 열어 확인한다.
Screenshot은 간격, 밀도, 정렬, 비율, 상태 UI를 맞추기 위한 실제 기준이다.

작업 순서:

1. 이 문서에서 route/page의 primary screenshot을 찾는다.
2. 이미지를 vision tool 또는 이미지 뷰어로 직접 확인한다.
3. `../../FRAME.md`의 해당 page/route 섹션을 확인한다.
4. `../../DESIGN.md`의 필요한 visual token/component 섹션만 확인한다.
5. 구현 후 같은 viewport로 캡처해 기준 screenshot과 비교한다.

## Screenshot path rule

이 문서의 경로는 `design/app-vue/` 기준 상대 경로다.

- Screenshot root: `../../edu_screnshot/`

## Dashboard

### `/dashboard`

Page:
- `src/pages/DashboardPage.vue`

Primary screenshot:
- `../../edu_screnshot/01_home_dashboard.png`

Secondary screenshot:
- `../../edu_screnshot/40_attendance_detail_from_home_more.png`

Inspect before coding:
- global header/nav
- dashboard card density
- attendance card and home more/detail transition
- curriculum/quest/resource preview sections
- footer and floating quick action

## Classroom

### `/classroom/curriculum`

Primary screenshot:
- `../../edu_screnshot/02_classroom_weekly_curriculum.png`

Secondary screenshot:
- `../../edu_screnshot/12_classroom_curriculum_week_toggle_attempt.png`

Inspect before coding:
- hero/banner height
- dark navy section tabs
- phase/semester stepper
- week/month toggle
- week carousel and curriculum timeline card spacing
- replay/material action buttons

### `/classroom/quests`

Primary screenshot:
- `../../edu_screnshot/03_quest_evaluation_list.png`

Secondary screenshot:
- `../../edu_screnshot/38_classroom_quest_detail_completed.png`

Inspect before coding:
- quest/evaluation row/card height
- planned/completed/pass/fail status UI
- exp/point display
- detail completed state
- pagination

### `/classroom/resources`

Primary screenshot:
- `../../edu_screnshot/04_learning_resources.png`

Inspect before coding:
- learning resource list/card layout
- thumbnail/title/meta alignment
- search/filter/pagination

### `/classroom/my-replays`

Primary screenshot:
- `../../edu_screnshot/13_classroom_my_lecture_replay.png`

### `/classroom/all-replays`

Primary screenshot:
- `../../edu_screnshot/14_classroom_all_lecture_replay.png`

### `/classroom/required-learning`

Primary screenshot:
- `../../edu_screnshot/15_classroom_required_learning_empty.png`

Inspect before coding:
- empty state icon/text/spacing
- action availability

## Community

### `/community/survey`

Primary screenshot:
- `../../edu_screnshot/05_community_survey.png`

### `/community/open-board`

Primary screenshot:
- `../../edu_screnshot/06_open_board.png`

Secondary screenshot:
- `../../edu_screnshot/35_community_open_board_write_form.png`

Inspect before coding:
- board list table
- search/filter/write button
- pagination
- write form table and attachment controls

### `/community/anonymous-board`

Primary screenshot:
- `../../edu_screnshot/16_community_anonymous_board.png`

### `/community/class-roster`

Primary screenshot:
- `../../edu_screnshot/17_community_class_roster.png`

## Helpdesk

### `/help/notice`

Primary screenshot:
- `../../edu_screnshot/07_helpdesk_notice.png`

Secondary screenshot:
- `../../edu_screnshot/33_helpdesk_notice_detail.png`

Inspect before coding:
- notice board table
- detail title/meta/content layout

### `/help/faq`

Primary screenshot:
- `../../edu_screnshot/08_helpdesk_faq_empty.png`

Inspect before coding:
- empty state treatment
- FAQ accordion placeholder if data exists later

### `/help/inquiries`

Primary screenshot:
- `../../edu_screnshot/09_helpdesk_inquiry_empty.png`

Secondary screenshot:
- `../../edu_screnshot/10_helpdesk_inquiry_form.png`

Inspect before coding:
- empty state
- inquiry write form table
- submit/cancel action alignment

### `/help/rules`

Primary screenshot:
- `../../edu_screnshot/18_helpdesk_academic_rules.png`

Secondary screenshot:
- `../../edu_screnshot/34_helpdesk_academic_rules_expanded_attendance.png`

Inspect before coding:
- academic rules accordion
- expanded attendance content spacing

## Mentoring

### `/mentoring/qna`

Primary screenshot:
- `../../edu_screnshot/19_mentoring_qna_board.png`

### `/mentoring/story`

Primary screenshot:
- `../../edu_screnshot/11_mentoring_story_board.png`

### `/mentoring/notice`

Primary screenshot:
- `../../edu_screnshot/20_mentoring_notice_board.png`

### `/mentoring/meetup/apply`

Primary screenshot:
- `../../edu_screnshot/21_mentoring_meetup_apply.png`

### `/mentoring/meetup/info`

Primary screenshot:
- `../../edu_screnshot/22_mentoring_meetup_info_empty.png`

### `/mentoring/meetup/reviews`

Primary screenshot:
- `../../edu_screnshot/23_mentoring_meetup_review_board.png`

Secondary screenshot:
- `../../edu_screnshot/24_mentoring_meetup_review_write_form.png`

Inspect before coding:
- mentoring board table/list pattern
- write form pattern
- meetup empty state

## MyCampus

### `/mycampus/level-points`

Primary screenshot:
- `../../edu_screnshot/25_mycampus_level_points_dashboard.png`

Inspect before coding:
- level/point summary cards
- point history/table/card density

### `/mycampus/attendance`

Primary screenshot:
- `../../edu_screnshot/26_mycampus_attendance_status.png`

### `/mycampus/learning/elearning`

Primary screenshot:
- `../../edu_screnshot/27_mycampus_learning_elearning_empty.png`

### `/mycampus/bookmarks`

Primary screenshot:
- `../../edu_screnshot/28_mycampus_bookmarks_empty.png`

### `/mycampus/documents`

Primary screenshot:
- `../../edu_screnshot/29_mycampus_document_submission_empty.png`

Secondary screenshot:
- `../../edu_screnshot/30_mycampus_document_submission_write_form.png`

### `/mycampus/pledge`

Primary screenshot:
- `../../edu_screnshot/31_mycampus_student_pledge_list.png`

### `/mycampus/education-status`

Primary screenshot:
- `../../edu_screnshot/32_mycampus_education_status.png`

## Notifications / profile / error

### `/notifications`

Primary screenshot:
- `../../edu_screnshot/36_notification_inbox.png`

Secondary screenshot:
- `../../edu_screnshot/37_profile_click_no_dropdown_notification_page.png`

Inspect before coding:
- notification inbox layout
- profile click behavior on notification page

### `/403`

Primary screenshot:
- `../../edu_screnshot/39_mycampus_ebook_403_forbidden.png`

Inspect before coding:
- forbidden message layout
- button style
- centered error state

## Full screenshot inventory

- `../../edu_screnshot/01_home_dashboard.png`
- `../../edu_screnshot/02_classroom_weekly_curriculum.png`
- `../../edu_screnshot/03_quest_evaluation_list.png`
- `../../edu_screnshot/04_learning_resources.png`
- `../../edu_screnshot/05_community_survey.png`
- `../../edu_screnshot/06_open_board.png`
- `../../edu_screnshot/07_helpdesk_notice.png`
- `../../edu_screnshot/08_helpdesk_faq_empty.png`
- `../../edu_screnshot/09_helpdesk_inquiry_empty.png`
- `../../edu_screnshot/10_helpdesk_inquiry_form.png`
- `../../edu_screnshot/11_mentoring_story_board.png`
- `../../edu_screnshot/12_classroom_curriculum_week_toggle_attempt.png`
- `../../edu_screnshot/13_classroom_my_lecture_replay.png`
- `../../edu_screnshot/14_classroom_all_lecture_replay.png`
- `../../edu_screnshot/15_classroom_required_learning_empty.png`
- `../../edu_screnshot/16_community_anonymous_board.png`
- `../../edu_screnshot/17_community_class_roster.png`
- `../../edu_screnshot/18_helpdesk_academic_rules.png`
- `../../edu_screnshot/19_mentoring_qna_board.png`
- `../../edu_screnshot/20_mentoring_notice_board.png`
- `../../edu_screnshot/21_mentoring_meetup_apply.png`
- `../../edu_screnshot/22_mentoring_meetup_info_empty.png`
- `../../edu_screnshot/23_mentoring_meetup_review_board.png`
- `../../edu_screnshot/24_mentoring_meetup_review_write_form.png`
- `../../edu_screnshot/25_mycampus_level_points_dashboard.png`
- `../../edu_screnshot/26_mycampus_attendance_status.png`
- `../../edu_screnshot/27_mycampus_learning_elearning_empty.png`
- `../../edu_screnshot/28_mycampus_bookmarks_empty.png`
- `../../edu_screnshot/29_mycampus_document_submission_empty.png`
- `../../edu_screnshot/30_mycampus_document_submission_write_form.png`
- `../../edu_screnshot/31_mycampus_student_pledge_list.png`
- `../../edu_screnshot/32_mycampus_education_status.png`
- `../../edu_screnshot/33_helpdesk_notice_detail.png`
- `../../edu_screnshot/34_helpdesk_academic_rules_expanded_attendance.png`
- `../../edu_screnshot/35_community_open_board_write_form.png`
- `../../edu_screnshot/36_notification_inbox.png`
- `../../edu_screnshot/37_profile_click_no_dropdown_notification_page.png`
- `../../edu_screnshot/38_classroom_quest_detail_completed.png`
- `../../edu_screnshot/39_mycampus_ebook_403_forbidden.png`
- `../../edu_screnshot/40_attendance_detail_from_home_more.png`
