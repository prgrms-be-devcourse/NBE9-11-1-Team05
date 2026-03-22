# NBE9-11-1-Team05
2026.3.19~2026.3.26 5팀 주문하시조.

---

## 공통

| 항목 | 값 |
|------|-----|
| 베이스 URL (로컬) | `http://localhost:8080` |
| 응답 형식 | `application/json; charset=UTF-8` |
| API 문서 (Swagger UI) | 앱 실행 후 springdoc 설정 경로 (예: `/swagger-ui.html`) |

### `orderStatus` (주문 상태)

`PROCESSING` · `SHIPPED` · `DELIVERED`

### 데이터 모델 참고

DB의 `Orders`는 **품목(커피) 1종 × 수량**당 **한 행**입니다. 한 번의 결제에 여러 품목이 있으면 응답 배열에 여러 요소가 올 수 있습니다.

---

## API 명세

### OWN-09 · 업주 전체 주문 목록

들어온 **모든 주문 라인**을 주문 시각 **최신순**으로 조회합니다.

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/owner/orders` |
| Query Params | 없음 |

**응답**

- `200 OK`
- Body: `OwnerOrderSummaryDto[]`
- 주문이 없으면 `[]`

**`OwnerOrderSummaryDto` 필드**

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | number | 주문(라인) ID |
| `email` | string | 주문 시 이메일 |
| `address` | string | 배송지 주소 |
| `zipCode` | string | 우편번호 |
| `orderedAt` | string | 주문 시각 (ISO-8601, 예: `2026-03-20T14:12:30`) |
| `totalAmount` | number | 해당 라인 총액 |
| `quantity` | number | 수량 |
| `orderStatus` | string | `orderStatus` enum |
| `coffeeName` | string \| null | 상품명 |

**예시 요청**

```http
GET /api/owner/orders HTTP/1.1
Host: localhost:8080
```

**예시 응답**

```json
[
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
]
```

---

### CUS-09 · 이메일만으로 주문 요약 목록

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/orders/by-email` |
| Query Params (필수) | `email` (이메일 형식) |

**응답**

- `200 OK`
- Body: `OrdersSummaryDto[]`

**`OrdersSummaryDto` 필드:** `id`, `email`, `orderedAt`, `totalAmount`, `quantity`, `orderStatus`

---

### CUS-09 · 주문 내역 조회 (이메일 + 우편번호)

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/orders/history` (권장) |
| Alias | `/api/orders/by-email-and-zip` |
| Query Params (필수) | `email`, `zipCode` (최대 20자) |

**응답**

- `200 OK`
- Body: `OrdersDetailDto[]`
- 조건에 해당하는 주문이 없으면 `[]`

**예시 요청**

```http
GET /api/orders/history?email=user@example.com&zipCode=12345 HTTP/1.1
Host: localhost:8080
```

**예시 응답**

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

### CUS-10 · 주문 1건 상세 조회 (주문번호)

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| Path | `/api/orders/{ordersId}` |
| Path 변수 | `ordersId`: 숫자만 |

**응답**

- `200 OK` — Body: `OrdersDetailDto`
- `404 Not Found` — 해당 ID 없음

**예시 요청**

```http
GET /api/orders/101 HTTP/1.1
Host: localhost:8080
```

---

### 응답 DTO: `OrdersDetailDto`

- **헤더:** `id`, `email`, `address`, `zipCode`, `orderedAt`, `totalAmount`, `orderStatus`
- **라인:** `items[]` — `coffeeId`, `coffeeName`, `quantity`, `unitPrice`, `lineTotal`

---

## Postman 테스트 예시

베이스 URL: `http://localhost:8080`

### 0) OWN-09 · 업주 전체 주문 목록

- Method: `GET`
- URL: `{{BASE_URL}}/api/owner/orders`

### 1) CUS-09 · 이메일만

- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/by-email`
- Params: `email`

### 2) CUS-09 · 이메일 + 우편번호

- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/history`
- Params: `email`, `zipCode`

Alias: `{{BASE_URL}}/api/orders/by-email-and-zip` (동일 Params)

예시 URL:

```text
http://localhost:8080/api/orders/history?email=user@example.com&zipCode=12345
```

### 3) CUS-10 · 주문 1건 상세

- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/{ordersId}`

예시 URL:

```text
http://localhost:8080/api/orders/101
```

**팁**

- 응답이 `[]`이면 조건에 맞는 주문 데이터가 없는 상태입니다.
- 업주 목록에 데이터가 없으면 `Orders` 테이블에 행이 아직 없거나, 주문 저장 API가 연동되지 않은 경우일 수 있습니다.
