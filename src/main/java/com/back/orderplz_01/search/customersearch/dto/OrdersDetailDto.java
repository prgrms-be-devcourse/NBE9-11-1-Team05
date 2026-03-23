package com.back.orderplz_01.search.customersearch.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdersDetailDto {

	private Integer id;

	private String email;

	private String address;

	private String zipCode;

	private LocalDateTime orderedAt;

	private Long totalAmount;

	private OrderStatus orderStatus;

	private List<OrdersItemDto> items;

	public static OrdersDetailDto from(Orders orders) {
		return new OrdersDetailDto(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt(),
			orders.getTotalAmount(),
			orders.getOrderStatus(),
			Collections.singletonList(toLineItem(orders))
		);
	}

	private static OrdersItemDto toLineItem(Orders orders) {
		Coffee coffee = orders.getCoffee();
		Integer coffeeId = coffee == null ? null : coffee.getId();
		Long unitPrice = coffee == null ? null : coffee.getPrice();
		Long quantity = orders.getQuantity();
		Long lineTotal = (unitPrice == null || quantity == null) ? null : unitPrice * quantity;

		return new OrdersItemDto(
			coffeeId,
			coffee == null ? null : coffee.getName(),
			quantity,
			unitPrice,
			lineTotal
		);
	}

	@Getter
	@AllArgsConstructor
	public static class OrdersItemDto {

		private Integer coffeeId;

		private String coffeeName;

		private Long quantity;

		private Long unitPrice;

		private Long lineTotal;
	}
}
