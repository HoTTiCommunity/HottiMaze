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
VALUES ('초보자를 위한 간단한 미로', 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/main.png', '2024-01-10 10:00:00', '2024-01-10 10:00:00', 245, 1, 'APPROVED', '2024-01-10 11:00:00', 1);

-- 미로 1번 문제들 (승인된 미로) - 힌트 추가
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/question1.png', 'test', 1, '첫 번째 문제', 10, '영어로 "시험"을 의미하는 단어입니다', '2024-01-10 10:00:00', '2024-01-10 10:00:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/question2.png', 'test', 2, '두 번째 문제', 10, '첫 번째 문제와 같은 답입니다', '2024-01-10 10:05:00', '2024-01-10 10:05:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/question3.png', 'test', 3, '세 번째 문제', 10, '4글자 영단어로 시작은 t입니다', '2024-01-10 10:10:00', '2024-01-10 10:10:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/question4.png', 'test', 4, '네 번째 문제', 10, '검사나 시험을 의미하는 영단어입니다', '2024-01-10 10:15:00', '2024-01-10 10:15:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, points, hint, created_at, updated_at)
VALUES (1, 'https://storage.googleapis.com/hotii-maze-imgs/mazes/maze1/question5.png', 'test', 5, '다섯 번째 문제', 10, '이전 문제들과 모두 같은 답입니다', '2024-01-10 10:20:00', '2024-01-10 10:20:00');

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

-- 미로 1번 완주자들
INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 2, '너무 쉽다', true, '2024-01-11 11:00:00', '2024-01-11 11:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 3, '이걸 문제라고 냈어??', true, '2024-01-11 15:00:00', '2024-01-11 15:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 4, '너무 재미있었어요', true, '2024-01-12 10:00:00', '2024-01-12 10:00:00');

INSERT INTO maze_review (maze_id, user_id, content, is_completed, created_at, updated_at)
VALUES (1, 5, '다시 만들도록', true, '2024-01-12 17:00:00', '2024-01-12 17:00:00');
