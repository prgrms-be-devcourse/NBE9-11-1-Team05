package com.back.orderplz_01.orders.dto.response;

import java.util.List;

/* CUS-09 주문별 정보를 리스트로 반환 (주문번호,일자,주문상태,주문라인아이템,주소,우편번호,총금액) */
/* 주문 한건당 1개의 주문 정보가 있음 */
public record OrdersSearchListRes(
	String email,
	List<OrdersSearchItemRes> orders
) {
}
