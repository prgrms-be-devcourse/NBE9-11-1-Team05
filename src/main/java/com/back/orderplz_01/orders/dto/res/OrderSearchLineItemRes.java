package com.back.orderplz_01.orders.dto.res;

public record OrderSearchLineItemRes(
	String coffeeName,
	Long quantity,
	Long price
) {
}

