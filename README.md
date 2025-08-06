# Gear4Camp

간단한 쇼핑몰 백엔드 포트폴리오 프로젝트입니다. (Java 17 · Spring Boot · MyBatis · JWT · Docker · GitHub Actions)

---

## 5-Min Quick Start

### 1) Run (둘 중 하나 선택)

#### A) Local (profile=local)
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### B) Docker (DB + App)
```bash
# 컨테이너 기동
docker compose up -d

# 상태 확인
docker compose ps

# 앱 로그 팔로우(서비스명이 spring-app인 경우)
docker compose logs -f spring-app
```
> 서비스명이 다르면 `spring-app`을 compose의 서비스명으로 변경.  
> 이미지 변경 후 재빌드는 `docker compose up -d --build` 사용.  
> 정리하려면 `docker compose down -v` 를 사용가능.

---

### 2) Swagger
- http://localhost:8080/swagger-ui/index.html

---

### 3) JWT 사용 흐름 (회원가입 → 로그인 → 토큰 → 보호 API)
```bash
# 회원가입
curl -X POST http://localhost:8080/users/register   -H "Content-Type: application/json"   -d '{"userId":"test01","password":"pass1234","name":"tester"}'

# 로그인 (accessToken 획득)
curl -X POST http://localhost:8080/auth/login   -H "Content-Type: application/json"   -d '{"userId":"test01","password":"pass1234"}'
# 응답에서 accessToken 값을 꺼내 아래 요청의 Authorization 헤더에 사용
```

---

### 4) 샘플 CURL
```bash
# 주문 생성
curl -X POST http://localhost:8080/orders   -H "Authorization: Bearer <ACCESS_TOKEN>"   -H "Content-Type: application/json"   -d '{"items":[{"productId":1,"quantity":2}]}'
```

```bash
# 주문 취소
curl -X PUT http://localhost:8080/orders/1/cancel   -H "Authorization: Bearer <ACCESS_TOKEN>"
```

```bash
# 주문 단건 조회
curl -X GET http://localhost:8080/orders/1   -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

### 5) 응답 정책
- **POST 생성:** `201 Created` + `{ "message": "...", "data": ... }`
- **조회/수정/취소:** `200 OK` + `{ "message": "...", "data"?: ... }`
- **본문 없음:** `204 No Content` *(body 없음)*
- **에러:** `400/401/403/404` → `{"errorCode":"...","message":"..."}`
