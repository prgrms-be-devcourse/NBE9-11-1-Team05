package com.back.orderplz_01.orders.dto;

import java.time.LocalDateTime;

import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdersSummaryDto {

	private Long id;

	private String email;

	private LocalDateTime orderedAt;

	private Long totalAmount;

	private Long quantity;

	private OrderStatus orderStatus;

	public static OrdersSummaryDto from(Orders orders) {
		return new OrdersSummaryDto(
			orders.getId(),
			orders.getEmail(),
			orders.getOrderedAt(),
			orders.getTotalAmount(),
			orders.getQuantity(),
			orders.getOrderStatus()
		);
	}
}

