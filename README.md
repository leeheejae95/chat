# Chat Service (Spring Boot + WebSocket/STOMP + OAuth2 + JPA)

실시간 채팅(방 생성/참여/메시지 저장/읽음 기준 새 메시지 표시)을 제공하는 예제 서비스입니다.  
**일반 사용자(User)는 OAuth2 로그인(카카오/구글)**, **상담사(Consultant)는 폼 로그인**으로 접근하며, 채팅은 **WebSocket(STOMP Pub/Sub)** 기반으로 송수신합니다.

---

## 한 줄 요약

**OAuth2로 인증한 사용자들이 채팅방에 참여하고, STOMP Pub/Sub로 실시간 메시지를 주고받으며, 메시지는 DB(MySQL)에 저장**됩니다.

---

## 1) What (무엇을 만들었나?)

### 핵심 기능
- **채팅방**
  - 채팅방 생성
  - 채팅방 참여/나가기
  - 참여한 채팅방 목록 조회
- **메시지**
  - 실시간 메시지 송수신 (WebSocket)
  - 메시지 DB 저장 및 조회
- **새 메시지 표시**
  - 방 별 `lastCheckedAt` 기준으로 **내가 확인 이후 올라온 메시지 존재 여부(hasNewMessage)** 계산
- **인증/권한**
  - 사용자: **OAuth2 로그인(카카오/구글)**
  - 상담사: **폼 로그인 + ROLE_CONSULTANT만 접근 허용(/consultants/**)**

---

## 2) Why (왜 이렇게 설계했나?)

### 2-1. 왜 WebSocket + STOMP(Pub/Sub)인가?
- 채팅은 HTTP처럼 “요청-응답”보다 **서버→클라이언트 푸시**가 핵심입니다.
- STOMP를 쓰면:
  - “채팅방 별 토픽”을 **구독(subscribe)** 하는 구조가 자연스럽고
  - 클라이언트는 **/pub** 으로 발행(publish), 서버/브로커가 **/sub** 구독자에게 브로드캐스트하는 형태로 확장성이 좋아집니다.
- 단순 WebSocket 핸들러도 함께 두어:
  - **WebSocketHandler 방식 vs STOMP 방식** 비교/학습이 가능합니다.

### 2-2. 왜 Spring Security + OAuth2인가?
- 채팅은 “누가 보냈는지”가 중요해서 **인증된 사용자 식별**이 필수입니다.
- OAuth2를 쓰면:
  - 사용자 비밀번호를 직접 다루지 않고(보안 리스크↓)
  - 외부 인증(카카오/구글) 기반으로 빠르게 회원 유입이 가능합니다.
- 상담사는 내부 운영자 성격이라:
  - OAuth2가 아니라 **폼 로그인 + ROLE 기반 접근 제어**가 더 직관적입니다.

### 2-3. 왜 JPA + MySQL인가?
- 채팅방/사용자/메시지/참여관계는 전형적인 관계형 모델입니다.
- JPA로:
  - 엔티티 중심으로 빠르게 구현하고,
  - 저장/조회 로직을 Repository로 분리해 유지보수성을 확보합니다.
- MySQL은 로컬 개발 환경에서 접근성이 좋고 운영 환경 이식도 쉽습니다.

---

## 3) How (어떻게 동작하나?)

## 3-1. 메시지 흐름(STOMP 기준)
1. 클라이언트 WebSocket 연결  
   - Endpoint: `ws://localhost:8080/stomp/chats`
2. 채팅방 구독  
   - `/sub/chats/{chatroomId}` 구독
3. 메시지 발행  
   - `/pub/chats/{chatroomId}` 로 publish
4. 서버 처리
   - `@MessageMapping("/chats/{chatroomId}")` 에서 메시지 수신
   - DB에 메시지 저장
   - `/sub/chats/{chatroomId}` 구독자에게 메시지 전파
   - 채팅방 업데이트 알림 `/sub/chats/updates` 로 전파 (새 메시지 아이콘, 인원수 등 갱신 목적)

---

## 3-2. 새 메시지(hasNewMessage) 계산 방식
- 회원이 채팅방을 이동/나갈 때 `lastCheckedAt` 갱신
- 채팅방 목록 조회 시:
  - `existsByChatroomIdAndCreatedAtAfter(chatroomId, lastCheckedAt)`  
  - → **내 마지막 확인 이후 메시지가 있으면 hasNewMessage=true**

---

## 4) Tech Stack (기술 스택 & 선택 이유)

- **Java 17 / Spring Boot 3.3**
  - 최신 기반으로 안정적이고, Spring 생태계와 호환성이 좋음
- **Spring Web**
  - 기본 REST 엔드포인트(채팅방 생성/입장/메시지 조회 등) 제공
- **Spring WebSocket + STOMP**
  - 채팅방 단위 Pub/Sub 구조 구현
- **Spring Security**
  - 인증/인가(권한) 일원화
- **OAuth2 Client (Kakao/Google)**
  - 사용자 로그인: 외부 인증으로 빠르고 안전하게 구현
- **Spring Data JPA**
  - 관계형 도메인(회원/방/메시지/매핑)을 엔티티로 관리
- **MySQL**
  - 로컬 개발 및 운영 친화적 RDB
- **Lombok**
  - 보일러플레이트 코드 최소화

---

## 5) Project Structure (프로젝트 구조)

```text
chat-service/
├── build.gradle
├── settings.gradle
├── http/
│   └── test.http                 # API 테스트 샘플
└── src/
    ├── main/
    │   ├── java/org/chat/chatservice/
    │   │   ├── configs/           # Security, WebSocket, STOMP 설정
    │   │   ├── controllers/       # REST + STOMP 컨트롤러
    │   │   ├── dto/               # API/STOMP 전송 DTO
    │   │   ├── entities/          # JPA 엔티티 (Member, ChatRoom, Message, Mapping)
    │   │   ├── enums/             # 도메인 Enum (Role, Gender)
    │   │   ├── handlers/          # Raw WebSocket 핸들러(TextWebSocketHandler)
    │   │   ├── repositories/      # JPA Repository
    │   │   ├── services/          # 비즈니스 로직 (채팅/상담사/OAuth2)
    │   │   └── vo/                # Security Principal(UserDetails, OAuth2User)
    │   └── resources/
    │       ├── application.yml    # DB/OAuth2 설정
    │       └── static/            # 간단한 테스트 UI (index.html, stomp.js 등)
    └── test/
        └── java/...               # 테스트 코드
