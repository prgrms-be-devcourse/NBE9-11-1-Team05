package com.back.orderplz_01.orders.dto.res;

import java.time.LocalDateTime;

/* OWN-09 주문 한 건 (주문번호, 이메일, 주문일시, 총금액) */
public record OrdersOwnerSearchItem(
	Long id,
	String email,
	LocalDateTime orderedAt,
	Long totalAmount
) {
}
