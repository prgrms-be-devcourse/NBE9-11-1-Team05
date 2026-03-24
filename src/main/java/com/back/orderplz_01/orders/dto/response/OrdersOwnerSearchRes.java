package com.back.orderplz_01.orders.dto.response;

import java.util.List;

/* OWN-09 전체 주문 목록 조회 DTO (주문번호, 이메일, 주문일시, 총금액 리스트) */
public record OrdersOwnerSearchRes(
	List<OrdersOwnerSearchItem> orders
) {
}
