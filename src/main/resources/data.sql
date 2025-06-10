-- 카테고리 더미 데이터 (한 줄씩)
INSERT INTO category (name) VALUES ('공지사항');
INSERT INTO category (name) VALUES ('자유게시판');
INSERT INTO category (name) VALUES ('질문과답변');
INSERT INTO category (name) VALUES ('감옥게시판');

-- 사용자 더미 데이터 (role 컬럼 추가)
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iIdIncKdKkmOpIt6zuq1.ZMaluY6', 1000, 0, 1, 'ADMIN');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('user001',   'user123',     500,  0, 1, 'USER');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('student01', 'student123',  300,  0, 1, 'USER');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('developer', 'dev123',      800,  0, 1, 'USER');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('designer',  'design123',   400,  0, 1, 'USER');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('manager',   'manager123',  600,  0, 1, 'ADMIN');
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK, role)
VALUES ('tester',    'test123',     200,  0, 1, 'USER');

-- 공지사항 카테고리 게시글 (category_id = 1)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('사이트 이용 안내', '사이트 이용에 관한 전반적인 안내사항입니다. 회원가입 후 다양한 게시판을 이용하실 수 있습니다.', 'admin', '2024-01-15 09:00:00', '2024-01-15 09:00:00', 150, 1, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('서버 점검 안내', '매주 화요일 오전 2시-4시까지 정기 서버 점검이 진행됩니다. 이용에 참고 바랍니다.', 'admin', '2024-01-20 14:30:00', '2024-01-20 14:30:00', 89, 1, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('새로운 기능 업데이트', '미로 게임 기능이 새롭게 추가되었습니다. 많은 이용 바랍니다!', 'admin', '2024-02-01 10:15:00', '2024-02-01 10:15:00', 203, 1, 0, 0);

-- 자유게시판 카테고리 게시글 (category_id = 2)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('안녕하세요 새로운 회원입니다', '처음 가입했는데 잘 부탁드립니다. 여러 활동 참여하고 싶어요!', 'user001', '2024-01-25 11:20:00', '2024-01-25 11:20:00', 45, 2, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('오늘 날씨가 정말 좋네요', '봄이 오는 것 같아서 기분이 좋습니다. 여러분은 어떠신가요?', 'student01', '2024-02-10 16:45:00', '2024-02-10 16:45:00', 67, 2, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('점심 메뉴 추천해주세요', '매일 점심 메뉴 고르기가 힘드네요. 맛있는 곳 아시는 분 추천 부탁드려요!', 'designer', '2024-02-15 12:00:00', '2024-02-15 12:00:00', 23, 2, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('주말 계획 있으신가요?', '이번 주말에 특별한 계획 있으신 분들 계신가요? 저는 영화 보러 갈 예정입니다.', 'tester', '2024-02-20 19:30:00', '2024-02-20 19:30:00', 34, 2, 0, 0);

-- 질문과답변 카테고리 게시글 (category_id = 3)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('Spring Boot 설정 관련 질문', 'application.properties 파일에서 데이터베이스 연결 설정하는 방법을 알고 싶습니다.', 'student01', '2024-01-30 13:15:00', '2024-01-30 13:15:00', 78, 3, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('Java 람다 표현식 사용법', '람다 표현식을 처음 배우는데 이해가 잘 안됩니다. 쉬운 예제 있을까요?', 'user001', '2024-02-05 15:20:00', '2024-02-05 15:20:00', 92, 3, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('React Hook 사용 시 주의사항', 'useState와 useEffect를 사용할 때 주의해야 할 점들이 있나요?', 'designer', '2024-02-12 10:40:00', '2024-02-12 10:40:00', 56, 3, 0, 0);

-- 감옥게시판 카테고리 게시글 (category_id = 4)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('웹 포트폴리오 사이트 제작', '개인 포트폴리오 웹사이트 제작 프로젝트입니다. React와 Spring Boot를 사용할 예정입니다.', 'developer', '2024-02-01 14:20:00', '2024-02-01 14:20:00', 156, 4, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('모바일 앱 개발 프로젝트', 'Flutter를 이용한 간단한 일정 관리 앱을 만들고 있습니다. 협업하실 분 환영합니다.', 'designer', '2024-02-10 11:45:00', '2024-02-10 11:45:00', 98, 4, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('미로 게임 개발 진행상황', 'HottiMaze 프로젝트의 현재 진행상황을 공유합니다. 피드백 환영합니다!', 'manager', '2024-02-18 16:10:00', '2024-02-18 16:10:00', 142, 4, 0, 0);

-- 미로 데이터 (상태 컬럼 추가)
-- 승인된 미로들
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id, status, approved_at, approved_by)
VALUES ('초보자를 위한 간단한 미로', '/static/imgs/mazes/maze1/main.png', '2024-01-10 10:00:00', '2024-01-10 10:00:00', 245, 1, 'APPROVED', '2024-01-10 11:00:00', 1);

INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id, status, approved_at, approved_by)
VALUES ('도전! 중급 미로 탈출', '/static/imgs/mazes/maze2/main.png', '2024-01-15 14:30:00', '2024-01-15 14:30:00', 189, 2, 'APPROVED', '2024-01-15 15:30:00', 1);

-- 승인 대기 중인 미로
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id, status, approved_at, approved_by)
VALUES ('극한의 어려움 - 고급 미로', '/static/imgs/mazes/maze3/main.png', '2024-01-20 16:45:00', '2024-01-20 16:45:00', 0, 3, 'APPROVED', '2024-01-15 15:30:00', 1);

-- 승인된 미로에 더 많은 예시 추가
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id, status, approved_at, approved_by)
VALUES ('숲속의 미스터리 미로', '/static/imgs/mazes/maze4/main.png', '2024-01-22 11:20:00', '2024-01-22 11:20:00', 298, 4, 'APPROVED', '2024-01-22 12:20:00', 6);

-- 미로 1번 문제들 (승인된 미로) - 힌트 추가
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question1.png', 'test', 1, '첫 번째 문제', 10, '영어로 "시험"을 의미하는 단어입니다', '2024-01-10 10:00:00', '2024-01-10 10:00:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question2.png', 'test', 2, '두 번째 문제', 10, '첫 번째 문제와 같은 답입니다', '2024-01-10 10:05:00', '2024-01-10 10:05:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question3.png', 'test', 3, '세 번째 문제', 10, '4글자 영단어로 시작은 t입니다', '2024-01-10 10:10:00', '2024-01-10 10:10:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question4.png', 'test', 4, '네 번째 문제', 10, '검사나 시험을 의미하는 영단어입니다', '2024-01-10 10:15:00', '2024-01-10 10:15:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question5.png', 'test', 5, '다섯 번째 문제', 10, '이전 문제들과 모두 같은 답입니다', '2024-01-10 10:20:00', '2024-01-10 10:20:00');

-- 미로 2번 문제들 (승인된 미로) - 힌트 추가
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question1.png', 'test', 1, '중급 문제 1', '이것도 "test"입니다', '2024-01-15 14:30:00', '2024-01-15 14:30:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question2.png', 'test', 2, '중급 문제 2', '첫 번째 문제와 동일한 답입니다', '2024-01-15 14:35:00', '2024-01-15 14:35:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question3.png', 'test', 3, '중급 문제 3', '모든 문제의 답이 같습니다', '2024-01-15 14:40:00', '2024-01-15 14:40:00');

-- 미로 3번 문제들 (승인 대기 중인 미로) - 힌트 없음
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question1.png', 'test', 1, '고급 문제 1', '2024-01-20 16:45:00', '2024-01-20 16:45:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question2.png', 'test', 2, '고급 문제 2', '2024-01-20 16:50:00', '2024-01-20 16:50:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question3.png', 'test', 3, '고급 문제 3', '2024-01-20 16:55:00', '2024-01-20 16:55:00');

-- 미로 4번 문제들 (승인된 미로) - 힌트 추가
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question1.png', 'test', 1, '숲속 문제 1', '숲 속에서도 답은 "test"입니다', '2024-01-22 11:20:00', '2024-01-22 11:20:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question2.png', 'test', 2, '숲속 문제 2', '나무들 사이에 숨어있는 4글자 영단어', '2024-01-22 11:25:00', '2024-01-22 11:25:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, hint, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question3.png', 'test', 3, '숲속 문제 3', '지금까지의 모든 답과 동일합니다', '2024-01-22 11:30:00', '2024-01-22 11:30:00');

-- 미로 1번에 대한 투표 (좋아요가 많은 미로)
INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 2, true, '2024-01-11 10:30:00', '2024-01-11 10:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 3, true, '2024-01-11 14:20:00', '2024-01-11 14:20:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 4, true, '2024-01-12 09:15:00', '2024-01-12 09:15:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 5, true, '2024-01-12 16:45:00', '2024-01-12 16:45:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 6, false, '2024-01-13 11:30:00', '2024-01-13 11:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (1, 7, true, '2024-01-13 15:20:00', '2024-01-13 15:20:00');

-- 미로 2번에 대한 투표 (중간 정도의 평가)
INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (2, 1, true, '2024-01-16 10:00:00', '2024-01-16 10:00:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (2, 3, true, '2024-01-16 14:30:00', '2024-01-16 14:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (2, 4, false, '2024-01-17 09:20:00', '2024-01-17 09:20:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (2, 5, true, '2024-01-17 16:15:00', '2024-01-17 16:15:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (2, 7, false, '2024-01-18 11:45:00', '2024-01-18 11:45:00');

-- 미로 3번에 대한 투표 (어려워서 평가가 갈리는 미로)
INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 1, true, '2024-01-21 10:30:00', '2024-01-21 10:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 2, false, '2024-01-21 14:20:00', '2024-01-21 14:20:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 4, false, '2024-01-22 09:15:00', '2024-01-22 09:15:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 5, true, '2024-01-22 16:45:00', '2024-01-22 16:45:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 6, false, '2024-01-23 11:30:00', '2024-01-23 11:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (3, 7, false, '2024-01-23 15:20:00', '2024-01-23 15:20:00');

-- 미로 4번에 대한 투표 (인기가 높은 미로)
INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 1, true, '2024-01-23 12:00:00', '2024-01-23 12:00:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 2, true, '2024-01-23 14:30:00', '2024-01-23 14:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 3, true, '2024-01-24 09:15:00', '2024-01-24 09:15:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 5, true, '2024-01-24 16:45:00', '2024-01-24 16:45:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 6, true, '2024-01-25 11:30:00', '2024-01-25 11:30:00');

INSERT INTO maze_vote (maze_id, user_id, is_like, created_at, updated_at)
VALUES (4, 7, true, '2024-01-25 15:20:00', '2024-01-25 15:20:00');

-- 미로 완주 기록 (리뷰 시스템용) - 빈 content로 완주만 표시
-- 미로 1번 완주자들
INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 2, '', true, '2024-01-11 11:00:00', '2024-01-11 11:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 3, '', true, '2024-01-11 15:00:00', '2024-01-11 15:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 4, '', true, '2024-01-12 10:00:00', '2024-01-12 10:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 5, '', true, '2024-01-12 17:00:00', '2024-01-12 17:00:00');

-- 미로 2번 완주자들
INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (2, 1, '', true, '2024-01-16 11:00:00', '2024-01-16 11:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (2, 3, '', true, '2024-01-16 15:00:00', '2024-01-16 15:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (2, 5, '', true, '2024-01-17 17:00:00', '2024-01-17 17:00:00');

-- 미로 3번 완주자들
INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (3, 1, '', true, '2024-01-21 11:00:00', '2024-01-21 11:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (3, 5, '', true, '2024-01-22 17:00:00', '2024-01-22 17:00:00');

-- 미로 4번 완주자들
INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (4, 1, '', true, '2024-01-23 13:00:00', '2024-01-23 13:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (4, 2, '', true, '2024-01-23 15:00:00', '2024-01-23 15:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (4, 3, '', true, '2024-01-24 10:00:00', '2024-01-24 10:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (4, 5, '', true, '2024-01-24 17:00:00', '2024-01-24 17:00:00');

-- 실제 리뷰들 (완주 기록에 내용 추가)
-- 미로 1번 리뷰들
UPDATE maze_review SET content = '정말 재미있는 미로였어요! 초보자에게 딱 맞는 난이도 같습니다. 문제도 적당히 어렵고 힌트도 도움이 많이 됐어요.', updated_at = '2024-01-11 12:00:00'
WHERE maze_id = 1 AND user_id = 2;

UPDATE maze_review SET content = '처음 해보는 미로 게임인데 생각보다 재미있네요! 다음엔 더 어려운 미로에 도전해보고 싶어요.', updated_at = '2024-01-11 16:00:00'
WHERE maze_id = 1 AND user_id = 3;

UPDATE maze_review SET content = '문제가 너무 쉬웠어요ㅠㅠ 좀 더 어려운 걸로 만들어주세요! 그래도 UI는 예쁘네요 👍', updated_at = '2024-01-12 11:00:00'
WHERE maze_id = 1 AND user_id = 4;

-- 미로 2번 리뷰들
UPDATE maze_review SET content = '중급 난이도라고 하더니 정말 적당히 어렵네요. 문제 풀면서 머리도 써야 하고 재미있었습니다!', updated_at = '2024-01-16 12:00:00'
WHERE maze_id = 2 AND user_id = 1;

UPDATE maze_review SET content = '1번 미로보다 확실히 어려워졌네요. 그래도 힌트가 있어서 끝까지 풀 수 있었어요. 성취감 굿!', updated_at = '2024-01-16 16:00:00'
WHERE maze_id = 2 AND user_id = 3;

-- 미로 3번 리뷰들 (어려운 미로라 리뷰가 적음)
UPDATE maze_review SET content = '와... 이건 정말 어렵네요. 고급자용이라더니 맞는 것 같아요. 몇 번 시도 끝에 겨우 성공했습니다.', updated_at = '2024-01-21 12:00:00'