# NBE9-11-1-Team05
2026.3.19~2026.3.26 5팀 주문하시조.

---

## API 명세 (CUS - 09 , CUS - 10 주문 조회)

### CUS-09 · 주문 내역 조회 (이메일 + 우편번호)
- Method: `GET`
- Path: `/api/orders/history` (권장)
- Alias: `/api/orders/by-email-and-zip`
- Query Params (필수)
  - `email`: 이메일 형식
  - `zipCode`: 우편번호 (빈 값 불가)

응답:
- `200 OK`
- Body: `OrdersDetailDto[]`
- 조건에 해당하는 주문이 없으면 `[]` (빈 배열)

예시 요청:
```http
GET /api/orders/history?email=user@example.com&zipCode=12345 HTTP/1.1
Host: localhost:8080
```

예시 응답:
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

### CUS-10 · 주문 1건 상세 조회 (주문번호)
- Method: `GET`
- Path: `/api/orders/{ordersId}`

응답:
- `200 OK`
- Body: `OrdersDetailDto`
- 해당 `ordersId`가 없으면 `404 Not Found`

예시 요청:
```http
GET /api/orders/101 HTTP/1.1
Host: localhost:8080
```

---

### 응답 DTO: `OrdersDetailDto`
- 주문 헤더: `id`, `email`, `address`, `zipCode`, `orderedAt`, `totalAmount`, `orderStatus`
- 주문 라인: `items[]`
  - `coffeeId`, `coffeeName`, `quantity`, `unitPrice`, `lineTotal`

---

## Postman 테스트 예시

베이스 URL:
- `http://localhost:8080`

### 1) CUS-09 · 주문 내역 조회 (이메일 + 우편번호)
- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/history`
- Query Params:
  - `email`: `user@example.com`
  - `zipCode`: `12345`

Alias(호환):
- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/by-email-and-zip`
- Query Params: `email`, `zipCode` 동일

예시(요청 URL):
```text
http://localhost:8080/api/orders/history?email=user@example.com&zipCode=12345
```

### 2) CUS-10 · 주문 1건 상세 조회 (주문번호)
- Method: `GET`
- URL: `{{BASE_URL}}/api/orders/{ordersId}`

예시(요청 URL):
```text
http://localhost:8080/api/orders/101
```

팁:
- Postman에서 `Params` 탭에 `email`, `zipCode`를 키로 넣으면 됩니다.
- 응답이 `[]`면 해당 이메일/우편번호 조건의 주문이 없는 상태입니다.
