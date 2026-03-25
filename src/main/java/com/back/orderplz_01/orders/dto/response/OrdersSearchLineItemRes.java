package com.back.orderplz_01.orders.dto.response;

/* CUS-09 주문한 아이템의 정보를 라인별로 생성 후 반환 (원두명, 수량, 가격) */
public record OrdersSearchLineItemRes(
	String coffeeName,
	Long quantity,
	Long price
) {
}
