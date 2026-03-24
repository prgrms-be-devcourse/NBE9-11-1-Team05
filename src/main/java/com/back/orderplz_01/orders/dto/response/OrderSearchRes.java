package com.back.orderplz_01.orders.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.back.orderplz_01.orders.entity.OrderStatus;

public record OrderSearchRes(
	Long orderId,
	LocalDateTime orderDate,
	OrderStatus orderStatus,
	List<OrderSearchLineItemRes> orderLines,
	String address,
	String zipCode,
	Long totalAmount
) {
}
