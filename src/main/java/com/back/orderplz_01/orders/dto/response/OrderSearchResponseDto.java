package com.back.orderplz_01.orders.dto.response;

import java.util.List;

import com.back.orderplz_01.orders.dto.res.OrderSearchRes;

public record OrderSearchResponseDto(
	String message,
	List<OrderSearchRes> orders
) {
}

