package com.back.orderplz_01.orders.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.back.orderplz_01.coffee.entity.Coffee;
import com.back.orderplz_01.orders.entity.Orders;
import com.back.orderplz_01.orders.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdersDetailDto {

	private Long id;

	private String email;

	private String address;

	private String zipCode;

	private LocalDateTime orderedAt;

	private Long totalAmount;

	private OrderStatus orderStatus;

	private List<OrdersItemDto> items;


	public static OrdersDetailDto from(Orders orders) {
		Coffee coffee = orders.getCoffee();

		Long coffeeId = coffee == null ? null : coffee.getId();
		Long unitPrice = coffee == null ? null : coffee.getPrice();
		Long quantity = orders.getQuantity();

		Long lineTotal = (unitPrice == null || quantity == null) ? null : unitPrice * quantity;

		OrdersItemDto item = new OrdersItemDto(
			coffeeId,
			coffee == null ? null : coffee.getName(),
			quantity,
			unitPrice,
			lineTotal
		);

		return new OrdersDetailDto(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt(),
			orders.getTotalAmount(),
			orders.getOrderStatus(),
			Collections.singletonList(item)
		);
	}

	@Getter
	@AllArgsConstructor
	public static class OrdersItemDto {

		private Long coffeeId;

		private String coffeeName;

		private Long quantity;

		private Long unitPrice;

		private Long lineTotal;
	}
}

