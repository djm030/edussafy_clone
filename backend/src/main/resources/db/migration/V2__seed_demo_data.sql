-- Edu SSAFY Clone Demo Seed Data
-- 목적: Docker/MySQL 통합 실행 후 주요 사용자·관리자 API를 바로 확인할 수 있는 최소 데모 데이터.
-- 주의: 로컬 개발/검증용 데이터이며 운영 배포 전에는 별도 운영 seed 정책을 사용한다.

SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- Demo users
-- ------------------------------------------------------------
INSERT INTO users (
    email, password, name, student_no, generation, region, class_no,
    phone_number, role, status, created_at, updated_at
) VALUES
('student@edussafy.local', '$2y$10$AHHy9pCT64EVMtlrAaQISuzbueIborjeIP1x8gPjynAVZHNHito2a', '김싸피', '1300001', 13, '서울', 1, '010-0000-0001', 'STUDENT', 'ACTIVE', NOW(), NOW()),
('mentor@edussafy.local', '$2y$10$AHHy9pCT64EVMtlrAaQISuzbueIborjeIP1x8gPjynAVZHNHito2a', '이멘토', NULL, 13, '서울', 1, '010-0000-0002', 'MENTOR', 'ACTIVE', NOW(), NOW()),
('operator@edussafy.local', '$2y$10$AHHy9pCT64EVMtlrAaQISuzbueIborjeIP1x8gPjynAVZHNHito2a', '박운영', NULL, 13, '서울', 1, '010-0000-0003', 'OPERATOR', 'ACTIVE', NOW(), NOW());

SET @student_id = (SELECT id FROM users WHERE email = 'student@edussafy.local');
SET @mentor_id = (SELECT id FROM users WHERE email = 'mentor@edussafy.local');
SET @operator_id = (SELECT id FROM users WHERE email = 'operator@edussafy.local');
SET @admin_id = (SELECT id FROM users WHERE email = 'admin');

INSERT INTO user_stats (
    user_id, scholarship_point, total_exp, level_name, level_no,
    attendance_rate, completed_learning_count, updated_at
) VALUES
(@student_id, 120, 350, '새싹 개발자', 2, 96.5, 1, NOW()),
(@mentor_id, 0, 0, '멘토', 1, NULL, 0, NOW());

-- ------------------------------------------------------------
-- Course, calendar, attendance
-- ------------------------------------------------------------
INSERT INTO courses (
    title, description, generation, region, class_no, instructor_name,
    status, start_date, end_date, created_at, updated_at
) VALUES (
    '13기 서울 1반 Java 전공반',
    'SSAFY EDU 클론 검증용 기본 과정',
    13,
    '서울',
    1,
    '최강사',
    'OPEN',
    '2026-01-05',
    '2026-12-18',
    NOW(),
    NOW()
);

SET @course_id = LAST_INSERT_ID();

INSERT INTO education_calendar_days (
    course_id, calendar_date, day_type, is_education_day, title, description, created_at, updated_at
) VALUES
(@course_id, '2026-05-11', 'EDUCATION', 1, '정규 교육일', '오전/오후 정규 수업', NOW(), NOW()),
(@course_id, '2026-05-12', 'EVENT', 1, '월말 평가 안내', 'Quest 및 평가 진행일', NOW(), NOW());

SET @calendar_day_id = (SELECT id FROM education_calendar_days WHERE course_id = @course_id AND calendar_date = '2026-05-11');

INSERT INTO attendance_records (
    user_id, course_id, calendar_day_id, attendance_date,
    check_in_at, check_out_at, status, issue_types, reason_status,
    reason_text, check_in_type, check_out_type, note, created_at, updated_at
) VALUES (
    @student_id,
    @course_id,
    @calendar_day_id,
    '2026-05-11',
    '2026-05-11 08:51:00',
    '2026-05-11 18:04:00',
    'NORMAL',
    JSON_ARRAY(),
    'NONE',
    NULL,
    'WEB',
    'WEB',
    'seed attendance',
    NOW(),
    NOW()
);

INSERT INTO point_transactions (
    user_id, transaction_type, point_amount, exp_amount, reason,
    target_type, target_id, created_by_id, created_at
) VALUES
(@student_id, 'EARN', 20, 50, '데모 학습 완료 보상', 'LEARNING_CONTENT', NULL, @operator_id, NOW()),
(@student_id, 'ADJUST', 100, 300, '초기 데모 포인트', 'ADMIN_SEED', NULL, @operator_id, NOW());

-- ------------------------------------------------------------
-- Board posts
-- ------------------------------------------------------------
SET @notice_category_id = (
    SELECT bc.id FROM board_categories bc JOIN boards b ON b.id = bc.board_id
    WHERE b.code = 'notice' AND bc.code = 'GENERAL' LIMIT 1
);
SET @faq_category_id = (
    SELECT bc.id FROM board_categories bc JOIN boards b ON b.id = bc.board_id
    WHERE b.code = 'faq' AND bc.code = 'GENERAL' LIMIT 1
);

INSERT INTO board_posts (
    category_id, user_id, title, content_type, content_text, content_html,
    view_count, like_count, comment_count, scrap_count, has_attachment,
    is_notice, is_deleted, created_at, updated_at
) VALUES
(@notice_category_id, @operator_id, 'SSAFY EDU 클론 데모 공지', 'HTML', '데모 환경 공지사항입니다.', '<p>데모 환경 공지사항입니다.</p>', 12, 0, 0, 0, 0, 1, 0, NOW(), NOW()),
(@faq_category_id, @operator_id, '비밀번호를 잊어버렸어요.', 'FAQ', '임시 비밀번호 발급 API를 사용합니다.', '<p>임시 비밀번호 발급 API를 사용합니다.</p>', 5, 0, 0, 0, 0, 0, 0, NOW(), NOW());

-- ------------------------------------------------------------
-- Course weeks, sessions, learning contents, tasks
-- ------------------------------------------------------------
INSERT INTO course_weeks (course_id, week_no, title, start_date, end_date, sort_order)
VALUES (@course_id, 19, 'Spring Boot API 통합 검증', '2026-05-11', '2026-05-15', 19);

SET @week_id = LAST_INSERT_ID();

INSERT INTO course_sessions (
    course_id, week_id, title, subtitle, session_type, session_date,
    start_at, end_at, instructor_name, location, live_url, replay_url,
    material_post_id, is_required, sort_order, created_at, updated_at
) VALUES
(@course_id, @week_id, '백엔드 API 실행 검증', 'MySQL/Flyway/Docker', 'LECTURE', '2026-05-11', '2026-05-11 09:00:00', '2026-05-11 12:00:00', '최강사', '서울 1반', NULL, NULL, NULL, 1, 1, NOW(), NOW()),
(@course_id, @week_id, 'API 검증 다시보기', 'Actuator/Swagger smoke test', 'REPLAY', '2026-05-11', NULL, NULL, '최강사', '온라인', NULL, 'https://example.com/replay/api-smoke', NULL, 0, 2, NOW(), NOW());

SET @session_id = (SELECT id FROM course_sessions WHERE course_id = @course_id AND title = '백엔드 API 실행 검증' LIMIT 1);
SET @learning_category_id = (SELECT id FROM learning_categories WHERE code = 'PROGRAMMING' LIMIT 1);

INSERT INTO learning_contents (
    course_id, session_id, category_id, title, description, content_type,
    thumbnail_file_id, content_url, duration_seconds, view_count, like_count,
    bookmark_count, download_count, is_required, open_at, close_at,
    created_by_id, created_at, updated_at
) VALUES
(@course_id, @session_id, @learning_category_id, 'Spring Boot API Smoke Test', '통합 실행 검증용 영상 콘텐츠', 'VIDEO', NULL, 'https://example.com/learning/smoke-test', 1800, 3, 1, 0, 0, 1, NOW(), NULL, @operator_id, NOW(), NOW()),
(@course_id, @session_id, @learning_category_id, 'Docker Compose 운영 노트', 'Docker/Nginx 구성 확인용 자료', 'FILE', NULL, 'https://example.com/files/docker-compose-note.pdf', NULL, 1, 0, 0, 1, 0, NOW(), NULL, @operator_id, NOW(), NOW());

SET @content_id = (SELECT id FROM learning_contents WHERE title = 'Spring Boot API Smoke Test' LIMIT 1);

INSERT INTO user_learning_progresses (
    user_id, content_id, progress_status, progress_rate, last_position_seconds,
    started_at, completed_at, last_accessed_at
) VALUES (
    @student_id, @content_id, 'COMPLETED', 100, 1800,
    NOW(), NOW(), NOW()
);

INSERT INTO user_content_interactions (user_id, content_id, interaction_type, created_at)
VALUES
(@student_id, @content_id, 'VIEW', NOW()),
(@student_id, @content_id, 'PLAY', NOW()),
(@student_id, @content_id, 'LIKE', NOW());

INSERT INTO course_tasks (
    course_id, session_id, survey_id, title, round_no, task_type,
    description, open_at, close_at, total_score, is_required, sort_order,
    created_at, updated_at
) VALUES (
    @course_id, @session_id, NULL, 'API 통합 검증 Quest', 1, 'QUEST',
    'Docker 환경에서 백엔드 smoke test를 수행합니다.',
    NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 100, 1, 1, NOW(), NOW()
);

SET @task_id = LAST_INSERT_ID();

INSERT INTO user_task_results (
    task_id, user_id, result_status, score, original_score, retake_score,
    attempt_count, answer_data, submitted_at, completed_at, updated_at
) VALUES (
    @task_id, @student_id, 'COMPLETED', 95.00, 95.00, NULL,
    1, JSON_OBJECT('memo', 'seed result'), NOW(), NOW(), NOW()
);

-- ------------------------------------------------------------
-- Survey/application, inquiry, notification, activity
-- ------------------------------------------------------------
SET @survey_category_id = (SELECT id FROM survey_categories WHERE code = 'SURVEY' LIMIT 1);

INSERT INTO surveys (
    category_id, title, description, form_type, open_at, close_at,
    is_required, capacity, event_start_at, event_end_at, event_type,
    location, selection_policy, linked_post_id, created_by_id, created_at, updated_at
) VALUES (
    @survey_category_id,
    '데모 만족도 설문',
    '통합 실행 검증용 설문입니다.',
    'SURVEY',
    NOW(),
    DATE_ADD(NOW(), INTERVAL 14 DAY),
    0,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    @operator_id,
    NOW(),
    NOW()
);

SET @survey_id = LAST_INSERT_ID();

INSERT INTO survey_questions (
    survey_id, question_no, question_text, question_type, options,
    is_required, sort_order
) VALUES
(@survey_id, 1, 'API 실행 검증 환경은 정상인가요?', 'SINGLE_CHOICE', JSON_ARRAY('예', '아니오'), 1, 1),
(@survey_id, 2, '추가 의견을 입력하세요.', 'TEXT', NULL, 0, 2);

INSERT INTO survey_participants (
    survey_id, user_id, answers, participant_status,
    submitted_at, cancelled_at, updated_at, created_at
) VALUES (
    @survey_id,
    @student_id,
    JSON_OBJECT('1', '예', '2', '정상 동작 확인'),
    'SUBMITTED',
    NOW(),
    NULL,
    NOW(),
    NOW()
);

INSERT INTO inquiries (
    user_id, category, title, content, status, answer_content,
    answered_by_id, answered_at, is_deleted, created_at, updated_at
) VALUES (
    @student_id,
    '시스템',
    '데모 문의입니다.',
    'Docker 실행 환경 확인 문의',
    'ANSWERED',
    '정상 확인되었습니다.',
    @operator_id,
    NOW(),
    0,
    NOW(),
    NOW()
);

INSERT INTO notifications (
    sender_id, receiver_id, title, content, notification_type,
    target_type, target_id, is_important, metadata, is_read, read_at, created_at
) VALUES (
    @operator_id,
    @student_id,
    '데모 환경 준비 완료',
    'MySQL/Flyway seed 데이터가 준비되었습니다.',
    'SYSTEM',
    'COURSE',
    @course_id,
    1,
    JSON_OBJECT('source', 'V2__seed_demo_data'),
    0,
    NULL,
    NOW()
);

INSERT INTO user_activity_records (
    user_id, activity_type, title, description, organization,
    activity_date, result_text, evidence_file_id, created_at, updated_at
) VALUES (
    @student_id,
    'SSAFY_ACTIVITY',
    '백엔드 통합 검증 참여',
    'Docker/Nginx/MySQL 실행 검증 seed 활동',
    'SSAFY',
    '2026-05-11',
    '완료',
    NULL,
    NOW(),
    NOW()
);
