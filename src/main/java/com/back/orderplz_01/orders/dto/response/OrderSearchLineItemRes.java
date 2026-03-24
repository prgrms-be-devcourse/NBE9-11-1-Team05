package com.back.orderplz_01.orders.dto.response;

public record OrderSearchLineItemRes(
	String coffeeName,
	Long quantity,
	Long price
) {
}
