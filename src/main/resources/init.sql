-- 채팅 세션 테이블 초기화
DELETE FROM chat_sessions;
ALTER TABLE chat_sessions ALTER COLUMN id RESTART WITH 1;

-- 메시지 테이블 초기화
DELETE FROM chat_messages;
ALTER TABLE chat_messages ALTER COLUMN id RESTART WITH 1;

-- 채팅 세션 초기 데이터
INSERT INTO chat_sessions (user_id, title, created_at, updated_at, is_deleted, is_pinned) VALUES
('user@example.com', '첫 번째 대화', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, false),
('user@example.com', '두 번째 대화', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, false);

-- 채팅 메시지 초기 데이터
INSERT INTO chat_messages (session_id, content, message_type, created_at) VALUES
(1, '안녕하세요!', 'USER', CURRENT_TIMESTAMP),
(1, '반갑습니다. 무엇을 도와드릴까요?', 'SYSTEM', CURRENT_TIMESTAMP),
(2, '오늘 날씨가 좋네요.', 'USER', CURRENT_TIMESTAMP),
(2, '네, 정말 좋은 날씨입니다.', 'SYSTEM', CURRENT_TIMESTAMP); 