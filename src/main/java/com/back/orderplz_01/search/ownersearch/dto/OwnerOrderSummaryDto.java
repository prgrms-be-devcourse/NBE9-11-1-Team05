package com.back.orderplz_01.search.ownersearch.dto;

import java.time.LocalDateTime;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerOrderSummaryDto {

	private Integer id;

	private String email;

	private String address;

	private String zipCode;

	private LocalDateTime orderedAt;

	private Long totalAmount;

	private Long quantity;

	private OrderStatus orderStatus;

	private String coffeeName;

	public static OwnerOrderSummaryDto from(Orders orders) {
		Coffee coffee = orders.getCoffee();
		return new OwnerOrderSummaryDto(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt(),
			orders.getTotalAmount(),
			orders.getQuantity(),
			orders.getOrderStatus(),
			coffee == null ? null : coffee.getName()
		);
	}
}
