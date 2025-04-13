# Chat Application

OpenAI API를 활용한 채팅 애플리케이션입니다.

## 기능

- 사용자별 채팅 세션 관리
- OpenAI API를 통한 실시간 채팅 응답
- 채팅 히스토리 조회 및 관리

## 기술 스택

- **Backend**: Spring Boot, Java
- **Database**: H2 Database
- **API**: OpenAI API
- **Frontend**: HTML, Tailwind CSS

## 시작하기

### 요구사항

- Java 17 이상
- Maven
- OpenAI API 키

### 설치 및 실행

1. 저장소 클론
   ```bash
   git clone [repository-url]
   cd chat
   ```

2. OpenAI API 키 설정
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

3. 애플리케이션 실행
   ```bash
   # 개발 환경
   ./mvnw spring-boot:run
   
   # 프로덕션 환경
   export SPRING_PROFILES_ACTIVE=prod
   ./mvnw spring-boot:run
   ```

4. 브라우저에서 접속
   ```
   http://localhost:8080
   ```

## 프로젝트 구조

```
src/main/java/com/example/chat/
├── app/
│   ├── controller/     # API 컨트롤러
│   ├── dto/            # 데이터 전송 객체
│   └── service/        # 애플리케이션 서비스
├── domain/
│   ├── entity/         # 도메인 엔티티
│   ├── repository/     # 리포지토리 인터페이스
│   └── service/        # 도메인 서비스
└── ChatApplication.java # 애플리케이션 진입점
```

## API 엔드포인트

- `POST /api/chat/sessions`: 새 채팅 세션 생성
- `GET /api/chat/sessions`: 사용자의 채팅 세션 목록 조회
- `GET /api/chat/sessions/{sessionId}/messages`: 채팅 메시지 조회
- `POST /api/chat/sessions/{sessionId}/messages`: 메시지 전송
- `DELETE /api/chat/sessions/{sessionId}`: 채팅 세션 삭제

## 환경 설정

- **개발 환경**: `application-dev.yml`
  - H2 메모리 데이터베이스 사용
  - 초기 데이터 로딩 활성화

- **프로덕션 환경**: `application-prod.yml`
  - H2 파일 데이터베이스 사용
  - 초기 데이터 로딩 비활성화

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.
