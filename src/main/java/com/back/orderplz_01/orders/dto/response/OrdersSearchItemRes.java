package com.back.orderplz_01.orders.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.back.orderplz_01.orders.entity.OrderStatus;

/* CUS-09 주문 정보 (주문번호,일자,주문상태,주문라인아이템,주소,우편번호,총금액) */
public record OrdersSearchItemRes(
	Long orderId,
	LocalDateTime orderedAt,
	OrderStatus orderStatus,
	List<OrdersSearchLineItemRes> orderLines,
	String email,
	String address,
	String zipCode,
	Long totalAmount
) {
}
