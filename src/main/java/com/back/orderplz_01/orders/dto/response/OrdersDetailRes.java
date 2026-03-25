package com.back.orderplz_01.orders.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.back.orderplz_01.orders.entity.OrderStatus;
import com.back.orderplz_01.orders.entity.Orders;

public record OrdersDetailRes(
	Long ordersId,
	String email,
	String address,
	String zipCode,
	LocalDateTime orderedAt,
	OrderStatus orderStatus,
	List<OrdersDetailCoffeeListRes> coffeeList
) {
	public static OrdersDetailRes from(Orders orders) {
		List<OrdersDetailCoffeeListRes> coffeeList = orders.getOrderItems().stream()
			.map(item -> new OrdersDetailCoffeeListRes(
				item.getCoffee().getName(),
				item.getPrice(),  // 주문 시점 가격 스냅샷
				item.getQuantity()
			))
			.toList();

		return new OrdersDetailRes(
			orders.getId(),
			orders.getEmail(),
			orders.getAddress(),
			orders.getZipCode(),
			orders.getOrderedAt(),
			orders.getOrderStatus(),
			coffeeList
		);
	}
}
