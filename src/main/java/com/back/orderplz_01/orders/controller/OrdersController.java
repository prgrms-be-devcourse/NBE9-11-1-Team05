package com.back.orderplz_01.orders.controller;

import java.util.List;

import com.back.orderplz_01.orders.dto.OrdersSummaryDto;
import com.back.orderplz_01.orders.service.OrdersService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "OrdersController", description = "주문 API")
@Validated
public class OrdersController {

	private final OrdersService ordersService;

	// OWN-09: 전체 주문 목록 조회
	@GetMapping
	public List<OrdersSummaryDto> getAllOrders() {
		return ordersService.findAll();
	}

	// CUS-09: 이메일 기준 주문 내역 조회
	@GetMapping("/by-email")
	public List<OrdersSummaryDto> getOrdersByEmail(
		@RequestParam @Email @NotBlank String email
	) {
		return ordersService.findByEmail(email);
	}
}
