package com.back.orderplz_01.orders.dto.res;

import java.time.LocalDateTime;

import com.back.orderplz_01.orders.entity.Orders;

public record OrdersDetailRes(
	Long ordersId,
	String email,
	String address,
	String zipCode,
	LocalDateTime orderedAt
) {
	public static OrdersDetailRes from(Orders orders) {
		return new OrdersDetailRes(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt()
		);
	}
}
