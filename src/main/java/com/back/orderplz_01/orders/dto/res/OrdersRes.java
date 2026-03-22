package com.back.orderplz_01.orders.dto.res;

import java.time.LocalDateTime;

public record OrdersRes(
	Long ordersId,
	String email,
	String address,
	String zipCode,
	LocalDateTime orderedAt
) {
}
