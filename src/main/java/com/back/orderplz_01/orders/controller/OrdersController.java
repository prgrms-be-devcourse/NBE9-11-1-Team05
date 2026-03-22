package com.back.orderplz_01.orders.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.orderplz_01.orders.dto.res.OrdersRes;
import com.back.orderplz_01.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderss")
@Tag(name = "OrdersController", description = "주문 API")
public class OrdersController {

	private final OrdersService ordersService;

	@GetMapping("/{ordersId}")
	public OrdersRes detail(@PathVariable Long ordersId) {
		return ordersService.ordersDetail(ordersId);
	}
}
