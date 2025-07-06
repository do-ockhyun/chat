-- 사용자 테이블
CREATE TABLE users (
    id NUMBER(19,0) PRIMARY KEY,
    emp_no VARCHAR2(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

-- 채팅 세션 테이블
CREATE TABLE chat_sessions (
    id NUMBER(19,0) PRIMARY KEY,
    user_id VARCHAR2(255) NOT NULL,
    title VARCHAR2(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0 NOT NULL,
    is_pinned NUMBER(1) DEFAULT 0 NOT NULL,
    is_shared NUMBER(1) DEFAULT 0 NOT NULL,
    share_token VARCHAR2(64) UNIQUE,
    shared_at TIMESTAMP,
    last_message_at TIMESTAMP
);

CREATE SEQUENCE chat_sessions_seq START WITH 1 INCREMENT BY 1;

-- 채팅 메시지 테이블
CREATE TABLE chat_messages (
    id NUMBER(19,0) PRIMARY KEY,
    session_id NUMBER(19,0) NOT NULL,
    content CLOB NOT NULL,
    message_type VARCHAR2(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_session FOREIGN KEY (session_id) REFERENCES chat_sessions(id)
);

CREATE SEQUENCE chat_messages_seq START WITH 1 INCREMENT BY 1; 