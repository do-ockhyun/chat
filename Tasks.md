
# ChatGPT Clone 기능정의

## 기술
- tailwindscc
- springboot
- vue.js
- H2

## 기능
- 인증화면
  * email만 입력 받아서 유저 식별로 사용 
  * 인증후 Model 넘겨서 Chat 화면 init
    * Model: Chat Session List

- 채팅화면 
  * Left: 채팅 목록
  * Center: 채팅창
    * 채팅창 하단에는 입력도구

- api
  * 새 대화방 생성 요청
  * 대화 전송
  * 과거 대화 조회
  * 대화방 삭제 요청  

## 구성
- Data
  * Entity
    * ChatSession
    * ChatMessage
    * User 정보는 따로 저장하지 않고, Chat Data에 'userId'에 email 값을 넣어서 소유주 확인 한다

  * Repository
    * ChatSession
    * ChatMessage

  * Service
    * ChatData Serivce

- Service
  * GPT Query 
    * 입력받은 Chat Message를 Rest 방식으로 호출하여 응답값 수신
  * Data 처리 
    * ChatData Service를 호출하여 조회,저장,삭제 

- Controller
  * ViewController
    * / GET
      * index.html 을 렌더링
    * / POST
      * email 을 수신하고, ChatSession을 조회하여
      * chat.html 에 Model로 전달, 렌더링 한다.
      * Session 정보에 email를 저장한다.

  * ApiController
    * /${sesssioId}/messages GET
      * chat SesssioId 를 받아서 저장된 chat message를 조회한다
    * /${sesssioId}/messages POST
      * chat SessionId, message 를 받아서 저장하고
      * GPT Query를 호출하여 LLM 의 응답값을 받아온다
        * Data에 저장하고
        * api의 응답값으로 전송한다.
    * /${sesssioId} DELETE
      * sessionId 의 대화를 삭제한다.
      * 나가기 기능
    * /new
      * 새로운 대화방을 만들고
      * sessionId를 전송한다.
      * 새로운 대화창을 표기한다.

- html (타임리프 템플릿)
  * index.html
    * email 입력
    * 시작하기 버튼
      * form submit 으로 email 전달
  * chat.html
    * 대화목록, 대화창 UI를 구성
      * 대화목록 상단에 '새 대화' 버튼, 새대화창 표시
        * /new
    * /api를 이용해 대화 조회, 전송
      * 상단에 나가기 버튼 표시
        * /${sesssioId} DELETE 

---
## 작업 순서
1. 프로젝트 기본 설정 및 의존성 구성
2. 데이터베이스 스키마 설계 및 구현
3. 기본 API 엔드포인트 구현
4. 프론트엔드 기본 UI 구현
5. GPT API 연동