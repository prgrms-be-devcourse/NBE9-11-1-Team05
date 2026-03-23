# NBE9-11-1-Team05

2026.3.19~2026.3.26 5팀 주문하시조.

---

## 공통

| 항목 | 값 |
|------|-----|
| 베이스 URL (로컬) | `http://localhost:8080` |
| 응답 형식 | `application/json; charset=UTF-8` |
| API 문서 (Swagger UI) | 앱 실행 후 springdoc 경로 (예: `/swagger-ui.html`) |

### 주문 상태 `orderStatus`

`PROCESSING` · `SHIPPED` · `DELIVERED`

### 데이터 모델 (조회 기준)

- `Orders` 테이블 **한 행** = 주문 라인 **한 건**(커피 1종 + 수량·금액 등).
- 여러 품목 주문은 행이 여러 개이거나, 추후 도메인 확장 시 별도 설계.

### 백엔드 구현 위치 (참고)

| 구분 | 패키지 | 설명 |
|------|--------|------|
| 고객 조회 API | `com.back.orderplz_01.search.customersearch` | CUS-09(이메일+우편번호 필수), CUS-10 |
| 업주 조회 API | `com.back.orderplz_01.search.ownersearch` | OWN-09 |
| 주문 도메인(원본) | `com.back.orderplz_01.orders` | 엔티티·`OrdersRepository` 스켈레톤 등 |

고객/업주 **조회 API 경로**는 아래 명세와 동일. (`/api/orders` 는 `search.customersearch`, `/api/owner/orders` 는 `search.ownersearch`.)

---

## API 명세

### OWN-09 · 업주 주문 목록 (페이지네이션)

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/owner/orders` |
| Query | `page` (선택, 기본 `0`), `size` (선택, 기본 `20`), `sort` (선택). 실제 정렬은 **주문 시각 `orderedAt` 내림차순**이 기본 동작. |

**응답 `200 OK`**

- Body: Spring **`Page`** JSON (배열이 아님).
- 목록 행은 **`content`** 배열의 각 원소 = `OwnerOrderSummaryDto`.
- 데이터 없음: `content: []`, `totalElements: 0` 등.

**`Page`에서 자주 쓰는 필드**

| 필드 | 설명 |
|------|------|
| `content` | `OwnerOrderSummaryDto[]` |
| `totalElements` | 전체 행 수 |
| `totalPages` | 전체 페이지 수 |
| `number` | 현재 페이지 (0부터) |
| `size` | 페이지 크기 |
| `first` / `last` | 첫·마지막 페이지 여부 |

**`OwnerOrderSummaryDto`**

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | number | 주문(행) ID |
| `email` | string | 주문 이메일 |
| `address` | string | 배송지 |
| `zipCode` | string | 우편번호 |
| `orderedAt` | string | 주문 시각 (ISO-8601) |
| `totalAmount` | number | 해당 행 총액 |
| `quantity` | number | 수량 |
| `orderStatus` | string | 주문 상태 enum |
| `coffeeName` | string \| null | 상품명 |

**요청 예시**

```http
GET /api/owner/orders?page=0&size=20 HTTP/1.1
Host: localhost:8080
```

**응답 예시 (구조만)**

```json
{
  "content": [
    {
      "id": 101,
      "email": "user@example.com",
      "address": "서울시 …",
      "zipCode": "12345",
      "orderedAt": "2026-03-20T14:12:30",
      "totalAmount": 24000,
      "quantity": 2,
      "orderStatus": "PROCESSING",
      "coffeeName": "에스프레소"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 20
}
```

---

### CUS-09 · 이메일 + 우편번호(둘 다 필수) — 접수 주문 여부·내역

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/orders/history` (권장) |
| Alias | `/api/orders/by-email-and-zip` |
| Query (**둘 다 필수**) | `email` (이메일 형식), `zipCode` (최대 20자) |

**동작**

- 입력한 **이메일·우편번호가 모두 일치하는** 접수 주문(행)만 조회합니다. 이메일만으로는 조회하지 않습니다.

**응답 `200 OK`**

- Body: `OrdersDetailDto[]`. 조건에 맞는 주문이 없으면 `[]`.

검증 실패 시 `400` (이메일 형식·빈 `zipCode` 등).

**예시**

```http
GET /api/orders/history?email=user@example.com&zipCode=12345 HTTP/1.1
Host: localhost:8080
```

```json
[
  {
    "id": 101,
    "email": "user@example.com",
    "address": "배송지 주소",
    "zipCode": "12345",
    "orderedAt": "2026-03-20T14:12:30",
    "totalAmount": 24000,
    "orderStatus": "PROCESSING",
    "items": [
      {
        "coffeeId": 1,
        "coffeeName": "에스프레소",
        "quantity": 2,
        "unitPrice": 12000,
        "lineTotal": 24000
      }
    ]
  }
]
```

---

### CUS-10 · 주문 1건 상세

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/orders/{ordersId}` |
| Path 변수 | `ordersId`: 숫자만 (정수 ID) |

**응답**

- `200 OK` — Body: `OrdersDetailDto` (단건)
- `404 Not Found` — 해당 ID 없음
- `400 Bad Request` — 잘못된 요청(예: ID 누락 등 서비스 검증 시)

```http
GET /api/orders/101 HTTP/1.1
Host: localhost:8080
```

---

### `OrdersDetailDto` 구조

| 구분 | 필드 |
|------|------|
| 헤더 | `id`, `email`, `address`, `zipCode`, `orderedAt`, `totalAmount`, `orderStatus` |
| 라인 | `items[]` — `coffeeId`, `coffeeName`, `quantity`, `unitPrice`, `lineTotal` |

현재 모델에서는 `items`가 **항상 1개 요소**(해당 `Orders` 행 기준)로 내려갑니다.

---

## Postman 테스트 예시

베이스 URL: `http://localhost:8080`

### 0) OWN-09 · 업주 전체 주문 목록

- Method: `GET`
- URL: `{{BASE_URL}}/api/owner/orders`
- Params (선택): `page` (기본 `0`), `size` (기본 `20`), `sort` (선택). 응답은 **배열이 아니라 `Page` JSON**이므로 목록은 **`content`** 필드에서 꺼내 씁니다.

### 1) CUS-09 · 이메일 + 우편번호 (둘 다 필수)

- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/history`
- Params: `email`, `zipCode` (누구 하나라도 빠지면 `400`)

Alias: `{{BASE_URL}}/api/orders/by-email-and-zip` (동일 Params)

예시 URL:

```text
http://localhost:8080/api/orders/history?email=user@example.com&zipCode=12345
```

### 2) CUS-10 · 주문 1건 상세

- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/{ordersId}`

예시 URL:

```text
http://localhost:8080/api/orders/101
```

**팁**

- 고객 조회 응답이 `[]`이면 조건에 맞는 주문 데이터가 없는 상태입니다.
- 업주 목록(OWN-09)은 루트가 배열이 아니라 **`content` 배열**을 사용합니다.
- 업주 API에 인증이 붙어 있다면 동일한 토큰/쿠키를 요청에 포함합니다.
- CORS는 `WebMvcConfig`의 `/api/**` 설정을 따릅니다.

---

## (참고) 원본 스켈레톤 엔드포인트

`orders` 패키지의 빈 `OrdersController`는 메인 브랜치와 동일하게 **`GET /orderss`** 만 매핑되어 있으며, 조회 비즈니스와는 무관합니다. 실제 조회는 위 **`/api/orders`**, **`/api/owner/orders`** 를 사용합니다.
