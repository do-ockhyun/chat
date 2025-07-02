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

## 주요 변경 및 추가 기능 (2024-06)

- **채팅 세션 공유**: 공개 링크로 세션을 공유하고, 읽기 전용으로 누구나 접근 가능
- **세션 고정(즐겨찾기)**: 채팅방을 상단에 고정하여 즐겨찾기 영역에 표시
- **최근 대화 정렬**: 채팅방 목록이 최근 대화 발생 순서대로 자동 정렬
- **UI 개선**: 사이드바가 '즐겨찾기(고정)'와 '최근 항목' 두 영역으로 분리
- **공유/고정 상태 실시간 반영**: 공유/고정/해제 시 UI와 목록이 즉시 갱신
- **lastMessageAt 필드 도입**: 마지막 대화 시각 기준으로 목록 정렬 및 표기

## 추가/변경된 API 엔드포인트

- `POST /api/chat/{sessionId}/share`: 채팅 세션 공유 시작(공유 토큰 반환)
- `POST /api/chat/{sessionId}/unshare`: 채팅 세션 공유 해제
- `GET /share/{shareToken}`: 공유 토큰으로 읽기 전용 세션 조회
- `PATCH /api/chat/{sessionId}/pin`: 세션 고정/해제

## DB/도메인 변경

- `ChatSession` 엔티티에 `isPinned`, `isShared`, `shareToken`, `lastMessageAt` 등 필드 추가
- 메시지 추가 시 `lastMessageAt` 자동 갱신
- 목록 조회 쿼리: 고정 우선, 그 다음 마지막 대화 시각 내림차순 정렬

## 프론트엔드 변경

- 사이드바가 '즐겨찾기(고정)'와 '최근 항목'으로 분리되어 표시
- 각 세션의 공유/고정 상태에 따라 메뉴 동적 표시
- 메시지 전송 시 목록이 자동으로 최신 순서로 정렬
- 공유 링크 복사, 공유 해제, 고정 해제 등 UX 개선

## 환경 설정

- **개발 환경**: `application-dev.yml`
  - H2 메모리 데이터베이스 사용
  - 초기 데이터 로딩 활성화

- **프로덕션 환경**: `application-prod.yml`
  - H2 파일 데이터베이스 사용
  - 초기 데이터 로딩 비활성화

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.
