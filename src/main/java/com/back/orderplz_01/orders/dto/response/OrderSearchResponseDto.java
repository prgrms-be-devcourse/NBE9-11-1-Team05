package com.back.orderplz_01.orders.dto.response;

import java.util.List;

public record OrderSearchResponseDto(
	List<OrderSearchRes> orders
) {
}
