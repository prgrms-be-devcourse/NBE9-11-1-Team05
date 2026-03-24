package com.back.orderplz_01.orders.dto.response;

import java.time.LocalDateTime;

import com.back.orderplz_01.orders.entity.Orders;

/* OWN-09 주문 한 건 (주문번호, 이메일, 주문일시, 총금액) */
public record OrdersOwnerSearchItem(
	Long id,
	String email,
	LocalDateTime orderedAt,
	Long totalAmount
) {
	public static OrdersOwnerSearchItem from(Orders order) {
		return new OrdersOwnerSearchItem(
			order.getId(),
			order.getEmail(),
			order.getOrderedAt(),
			order.getTotalAmount()
		);
	}
}
