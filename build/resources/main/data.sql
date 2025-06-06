-- 카테고리 더미 데이터 (한 줄씩)
INSERT INTO category (name) VALUES ('공지사항');
INSERT INTO category (name) VALUES ('자유게시판');
INSERT INTO category (name) VALUES ('질문과답변');
INSERT INTO category (name) VALUES ('감옥 Confirm게시판');
INSERT INTO category (name) VALUES ('감옥게시판');

-- 사용자 더미 데이터 (한 줄씩)
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('admin',     'password123', 1000, 0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('user001',   'user123',     500,  0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('student01', 'student123',  300,  0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('developer','dev123',       800,  0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('designer',  'design123',   400,  0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('manager',   'manager123',  600,  0, 1);
INSERT INTO users (username, password, point, chulcheck, IS_AVAILABLE_CHULCHECK)
VALUES ('tester',    'test123',     200,  0, 1);

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

-- 감옥 Confirm게시판 카테고리 게시글 (category_id = 4)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('알고리즘 스터디 모집', '매주 토요일 오후 2시에 알고리즘 문제 풀이 스터디를 진행합니다. 참여하실 분 댓글 남겨주세요!', 'developer', '2024-01-28 20:00:00', '2024-01-28 20:00:00', 125, 4, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('Java 기초 스터디 시작', '자바 기초부터 차근차근 공부할 스터디원을 모집합니다. 초보자 환영!', 'manager', '2024-02-08 18:30:00', '2024-02-08 18:30:00', 87, 4, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('데이터베이스 공부 모임', 'SQL과 데이터베이스 설계에 대해 함께 공부하실 분들 모집합니다.', 'developer', '2024-02-14 21:15:00', '2024-02-14 21:15:00', 64, 4, 0, 0);

-- 감옥게시판 카테고리 게시글 (category_id = 5)
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('웹 포트폴리오 사이트 제작', '개인 포트폴리오 웹사이트 제작 프로젝트입니다. React와 Spring Boot를 사용할 예정입니다.', 'developer', '2024-02-01 14:20:00', '2024-02-01 14:20:00', 156, 5, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('모바일 앱 개발 프로젝트', 'Flutter를 이용한 간단한 일정 관리 앱을 만들고 있습니다. 협업하실 분 환영합니다.', 'designer', '2024-02-10 11:45:00', '2024-02-10 11:45:00', 98, 5, 0, 0);
INSERT INTO post (title, content, author, created_at, updated_at, view_count, category_id, gaechu, bechu) VALUES ('미로 게임 개발 진행상황', 'HottiMaze 프로젝트의 현재 진행상황을 공유합니다. 피드백 환영합니다!', 'manager', '2024-02-18 16:10:00', '2024-02-18 16:10:00', 142, 5, 0, 0);

-- 미로 데이터 (중복 제거 - 한 번만 삽입)
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id) VALUES ('초보자를 위한 간단한 미로', '/static/imgs/mazes/maze1/main.png', '2024-01-10 10:00:00', '2024-01-10 10:00:00', 245, 1);
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id) VALUES ('도전! 중급 미로 탈출', '/static/imgs/mazes/maze2/main.png', '2024-01-15 14:30:00', '2024-01-15 14:30:00', 189, 2);
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id) VALUES ('극한의 어려움 - 고급 미로', '/static/imgs/mazes/maze3/main.png', '2024-01-20 16:45:00', '2024-01-20 16:45:00', 156, 3);
INSERT INTO maze (maze_title, maze_dir, created_at, updated_at, view_count, user_id) VALUES ('숲속의 미스터리 미로', '/static/imgs/mazes/maze4/main.png', '2024-01-25 11:20:00', '2024-01-25 11:20:00', 298, 4);

-- 미로 1번 문제들
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question1.png', 'test', 1, '첫 번째 문제', '2024-01-10 10:00:00', '2024-01-10 10:00:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question2.png', 'test', 2, '두 번째 문제', '2024-01-10 10:05:00', '2024-01-10 10:05:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (1, '/static/imgs/mazes/maze1/question3.png', 'test', 3, '세 번째 문제', '2024-01-10 10:10:00', '2024-01-10 10:10:00');

-- 미로 2번 문제들
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question1.png', 'test', 2, '중급 문제 1', '2024-01-15 14:30:00', '2024-01-15 14:30:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question2.png', 'test', 2, '중급 문제 2', '2024-01-15 14:35:00', '2024-01-15 14:35:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (2, '/static/imgs/mazes/maze2/question3.png', 'test', 3, '중급 문제 3', '2024-01-15 14:40:00', '2024-01-15 14:40:00');

-- 미로 3번 문제들
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question1.png', 'test', 1, '고급 문제 1', '2024-01-20 16:45:00', '2024-01-20 16:45:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question2.png', 'test', 2, '고급 문제 2', '2024-01-20 16:50:00', '2024-01-20 16:50:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (3, '/static/imgs/mazes/maze3/question3.png', 'test', 3, '고급 문제 3', '2024-01-20 16:55:00', '2024-01-20 16:55:00');

-- 미로 4번 문제들
INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question1.png', 'test', 1, '숲속 문제 1', '2024-01-25 11:20:00', '2024-01-25 11:20:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question2.png', 'test', 2, '숲속 문제 2', '2024-01-25 11:25:00', '2024-01-25 11:25:00');

INSERT INTO maze_question (maze_id, question_image, correct_answer, question_order, title, created_at, updated_at)
VALUES (4, '/static/imgs/mazes/maze4/question3.png', 'test', 3, '숲속 문제 3', '2024-01-25 11:30:00', '2024-01-25 11:30:00');