package com.back.orderplz_01.orders.dto.response;

public record OrdersDetailCoffeeListRes(
	String name,
	Long price,
	Long quantity
) {
}
